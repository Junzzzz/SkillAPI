package com.github.junzzzz.skillapi.packet.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.junzzzz.skillapi.common.SkillRuntimeException;
import com.github.junzzzz.skillapi.packet.serializer.JsonPacketSerializer;
import com.github.junzzzz.skillapi.packet.serializer.PacketSerializer;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
        CallbackResultPacket<T> packet = new CallbackResultPacket<>(uuid, result, getDataType(this));

        if (from.isClient()) {
            Packet.sendToClient(packet, (EntityPlayerMP) player);
        } else {
            Packet.sendToServer(packet);
        }
    }

    protected abstract T returns(EntityPlayer player, Side from);

    private static ParameterizedDataType getDataType(CallbackPacket<?> packet) {
        ParameterizedType superclass = (ParameterizedType) packet.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
            throw new SkillRuntimeException("Unknown Error");
        }
        return ParameterizedDataType.get(actualTypeArguments[0]);
    }

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
