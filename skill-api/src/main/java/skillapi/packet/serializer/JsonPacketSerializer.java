package skillapi.packet.serializer;

import io.netty.buffer.ByteBuf;
import skillapi.packet.base.AbstractPacket;
import skillapi.utils.JsonUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author Jun
 */
public class JsonPacketSerializer implements PacketSerializer<AbstractPacket> {
    @Override
    public void serialize(AbstractPacket packet, ByteBuf buffer) throws Exception {
        buffer.writeBytes(JsonUtils.getMapper().writeValueAsString(packet).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public AbstractPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        return (AbstractPacket) JsonUtils.getMapper().readValue(new String(bytes, StandardCharsets.UTF_8), packetClass);
    }
}
