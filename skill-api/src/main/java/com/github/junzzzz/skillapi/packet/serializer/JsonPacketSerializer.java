package com.github.junzzzz.skillapi.packet.serializer;

import com.github.junzzzz.skillapi.packet.base.AbstractPacket;
import com.github.junzzzz.skillapi.utils.JsonUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author Jun
 */
public class JsonPacketSerializer implements PacketSerializer<AbstractPacket> {
    @Override
    public void serialize(AbstractPacket packet, ByteBuf buffer) throws Exception {
        PacketSerializer.writeString(buffer, JsonUtils.getMapper().writeValueAsString(packet));
    }

    @Override
    public AbstractPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        return (AbstractPacket) JsonUtils.getMapper().readValue(PacketSerializer.readString(buffer), packetClass);
    }
}
