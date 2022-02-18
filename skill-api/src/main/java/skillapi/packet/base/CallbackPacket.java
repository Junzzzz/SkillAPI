package skillapi.packet.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import skillapi.packet.serializer.JsonPacketSerializer;
import skillapi.packet.serializer.PacketSerializer;

import java.util.UUID;

/**
 * @author Jun
 */
public abstract class CallbackPacket<T> extends AbstractPacket {
    @JsonIgnore
    private UUID uuid;

    void setUUid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    protected final void run(EntityPlayer player, Side from) {
        T result = returns(player, from);
        if (result == null) {
            return;
        }
        CallbackResultPacket<T> packet = new CallbackResultPacket<>(uuid, result);

        if (from.isClient()) {
            Packet.sendToClient(packet, (EntityPlayerMP) player);
        } else {
            Packet.sendToServer(packet);
        }
    }

    protected abstract T returns(EntityPlayer player, Side from);

    public static class Serializer implements PacketSerializer<CallbackPacket<?>> {
        private final JsonPacketSerializer json = Packet.getSerializer(JsonPacketSerializer.class);

        @Override
        public void serialize(CallbackPacket<?> packet, ByteBuf buffer) throws Exception {
            buffer.writeLong(packet.uuid.getMostSignificantBits());
            buffer.writeLong(packet.uuid.getLeastSignificantBits());
            json.serialize(packet, buffer);
        }

        @Override
        public CallbackPacket<?> deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
            long m = buffer.readLong();
            long l = buffer.readLong();
            UUID uuid = new UUID(m, l);

            CallbackPacket<?> packet = (CallbackPacket<?>) json.deserialize(packetClass, buffer);
            packet.uuid = uuid;

            return packet;
        }
    }
}
