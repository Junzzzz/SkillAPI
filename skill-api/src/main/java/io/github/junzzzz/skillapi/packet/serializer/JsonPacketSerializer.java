package io.github.junzzzz.skillapi.packet.serializer;

import io.github.junzzzz.skillapi.packet.base.AbstractPacket;
import io.github.junzzzz.skillapi.utils.JsonUtils;
import io.netty.buffer.ByteBuf;

import static io.github.junzzzz.skillapi.packet.serializer.PacketSerializer.readString;
import static io.github.junzzzz.skillapi.packet.serializer.PacketSerializer.writeString;

/**
 * @author Jun
 */
public class JsonPacketSerializer implements PacketSerializer<AbstractPacket> {
    @Override
    public void serialize(AbstractPacket packet, ByteBuf buffer) throws Exception {
        writeString(buffer, JsonUtils.getMapper().writeValueAsString(packet));
    }

    @Override
    public AbstractPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        return (AbstractPacket) JsonUtils.getMapper().readValue(readString(buffer), packetClass);
    }
}
