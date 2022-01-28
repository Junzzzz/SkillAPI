package skillapi.packet.base;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import skillapi.api.annotation.SkillPacket;
import skillapi.common.SkillLog;
import skillapi.packet.serializer.PacketSerializer;
import skillapi.utils.ClassUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author Jun
 */
public final class Packet {
    private static final String CHANNEL_NAME = "SkillPacketChannel";
    private static final FMLEventChannel CHANNEL;

    private static final Map<Class<? extends PacketSerializer<? extends AbstractPacket>>, PacketSerializer<?
            extends AbstractPacket>> SERIALIZER_MAP = new HashMap<>(4);
    private static final Map<Class<? extends AbstractPacket>, IPacket> PACKET_MAP = new HashMap<>(16);
    private static IPacket[] packets;

    private static final Map<UUID, Consumer<?>> CALLBACK_MAP = new ConcurrentHashMap<>(4);

    static {
        CHANNEL = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL_NAME);
        CHANNEL.register(new PacketReceiveEvent());
    }

    public static void init() {
        packets = PACKET_MAP.values().toArray(new IPacket[0]);
        for (int i = 0; i < packets.length; i++) {
            packets[i].index = i;
        }
    }

    public static void register(Class<? extends AbstractPacket> clz, SkillPacket annotation) {
        Class<? extends PacketSerializer<? extends AbstractPacket>> serializerClass = annotation.serializer();
        PacketSerializer<? extends AbstractPacket> packetSerializer = SERIALIZER_MAP.get(serializerClass);
        if (packetSerializer == null) {
            packetSerializer = ClassUtils.newEmptyInstance(serializerClass, "Failed to create serializer: %s",
                    serializerClass.getName());
            SERIALIZER_MAP.put(serializerClass, packetSerializer);
        }
        PACKET_MAP.put(clz, new IPacket(clz, packetSerializer));
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketSerializer<? extends AbstractPacket>> T getSerializer(Class<T> clz) {
        PacketSerializer<? extends AbstractPacket> packetSerializer = SERIALIZER_MAP.get(clz);
        if (packetSerializer == null) {
            packetSerializer = ClassUtils.newEmptyInstance(clz, "Failed to create serializer: %s", clz.getName());
            SERIALIZER_MAP.put(clz, packetSerializer);
        }
        return (T) packetSerializer;
    }

    public static PacketSerializer<? extends AbstractPacket> getPacketSerializer(Class<? extends AbstractPacket> clz) {
        return SERIALIZER_MAP.get(clz);
    }

    public static void send(AbstractPacket packet) {
        sendToServer(packet);
    }

    public static void send(AbstractPacket packet, EntityPlayerMP player) {
        sendToClient(packet, player);
    }

    public static <T> void callback(CallbackPacket<T> packet, Consumer<T> consumer) {
        callback(packet, consumer, true);
    }

    @SuppressWarnings("unchecked")
    static <T> void callback(UUID uuid, T result) {
        Consumer<T> consumer = (Consumer<T>) CALLBACK_MAP.remove(uuid);
        if (consumer == null) {
            return;
        }
        consumer.accept(result);
    }

    /**
     * Send callback packet
     *
     * @param packet   Callback packet
     * @param consumer Callback function
     * @param force    Whether to force the execution of the callback function when the server thread is suspended in stand-alone mode
     * @param <T>      Type of result entity
     */
    public static <T> void callback(CallbackPacket<T> packet, Consumer<T> consumer, boolean force) {
        if (force && Minecraft.getMinecraft().isIntegratedServerRunning()) {
            String name = FMLClientHandler.instance().getClientPlayerEntity().getCommandSenderName();
            EntityPlayer player = FMLServerHandler.instance().getServer().getEntityWorld().getPlayerEntityByName(name);
            T result = packet.returns(player, Side.CLIENT);
            consumer.accept(result);
        } else {
            saveCallback(packet, consumer);
            sendToServer(packet);
        }

    }

    public static <T> void callback(CallbackPacket<T> packet, EntityPlayerMP player, Consumer<T> consumer) {
        saveCallback(packet, consumer);
        sendToClient(packet, player);
    }

    private static <T> void saveCallback(CallbackPacket<T> packet, Consumer<T> consumer) {
        UUID uuid = UUID.randomUUID();
        packet.setUUid(uuid);
        CALLBACK_MAP.put(uuid, consumer);
    }

    public static void sendToClient(AbstractPacket packet, EntityPlayerMP player) {
        FMLProxyPacket proxy = proxy(packet, Side.CLIENT);
        if (proxy != null) {
            CHANNEL.sendTo(proxy, player);
        } else {
            SkillLog.error("Unknown Error: packet [%s] is not registered", packet.getClass().getName());
        }
    }

    public static void sendToServer(AbstractPacket packet) {
        FMLProxyPacket proxy = proxy(packet, Side.SERVER);
        if (proxy != null) {
            CHANNEL.sendToServer(proxy);
        } else {
            SkillLog.error("Unknown Error: packet [%s] is not registered", packet.getClass().getName());
        }
    }

    public static void sendToDimension(AbstractPacket packet, int dimensionId) {
        FMLProxyPacket proxy = proxy(packet, Side.SERVER);
        if (proxy != null) {
            CHANNEL.sendToDimension(proxy, dimensionId);
        } else {
            SkillLog.error("Unknown Error: packet [%s] is not registered", packet.getClass().getName());
        }
    }

    public static void sendToAllAround(AbstractPacket packet, NetworkRegistry.TargetPoint point) {
        FMLProxyPacket proxy = proxy(packet, Side.SERVER);
        if (proxy != null) {
            CHANNEL.sendToAllAround(proxy, point);
        } else {
            SkillLog.error("Unknown Error: packet [%s] is not registered", packet.getClass().getName());
        }
    }

    public static void sendToAll(AbstractPacket packet) {
        FMLProxyPacket proxy = proxy(packet, Side.SERVER);
        if (proxy != null) {
            CHANNEL.sendToAll(proxy);
        } else {
            SkillLog.error("Unknown Error: packet [%s] is not registered", packet.getClass().getName());
        }
    }

    @SuppressWarnings("unchecked")
    private static FMLProxyPacket proxy(AbstractPacket packet, Side target) {
        IPacket packetInfo = PACKET_MAP.get(packet.getClass());
        if (packetInfo == null) {
            return null;
        }

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(packetInfo.index);
        try {
            packetInfo.serializer.serialize(packet, buffer);
        } catch (Exception e) {
            SkillLog.error(e, "Failed to serialize packet data. Packet class: %s", packet.getClass());
            return null;
        }
        FMLProxyPacket fmlProxyPacket = new FMLProxyPacket(buffer, CHANNEL_NAME);
        fmlProxyPacket.setTarget(target);
        return fmlProxyPacket;
    }

    static final class IPacket {
        int index;
        Class<? extends AbstractPacket> clz;

        @SuppressWarnings("rawtypes")
        PacketSerializer serializer;

        public IPacket(Class<? extends AbstractPacket> clz, PacketSerializer<? extends AbstractPacket> serializer) {
            this.clz = clz;
            this.serializer = serializer;
        }
    }

    public static final class PacketReceiveEvent {
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void onClientPacketEvent(FMLNetworkEvent.ClientCustomPacketEvent event) {
            process(event.packet.payload(), FMLClientHandler.instance().getClient().thePlayer, Side.SERVER);
        }

        @SubscribeEvent
        public void onServerPacketEvent(FMLNetworkEvent.ServerCustomPacketEvent event) {
            process(event.packet.payload(), ((NetHandlerPlayServer) event.handler).playerEntity, Side.CLIENT);
        }

        @SuppressWarnings("unchecked")
        private void process(ByteBuf buffer, EntityPlayer player, Side from) {
            int packetIndex = buffer.readInt();
            if (packetIndex < 0 || packetIndex >= packets.length) {
                // Ignore
                return;
            }
            IPacket packet = packets[packetIndex];
            AbstractPacket calledPacket;
            try {
                calledPacket = packet.serializer.deserialize(packet.clz, buffer);
                calledPacket.run(player, from);
            } catch (Exception e) {
                SkillLog.error(e, "Failed to deserialize packet data. Packet class: %s", packet.getClass());
            }
        }
    }
}
