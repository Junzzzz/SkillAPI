package skillapi.packet;

import io.netty.buffer.ByteBuf;
import skillapi.utils.JsonUtils;

/**
 * @author Jun
 */
public class JsonPacketSerializer implements PacketSerializer {
    @Override
    public byte[] serialize(AbstractPacket packet) throws Exception {
        return JsonUtils.getMapper().writeValueAsBytes(packet);
    }

    @Override
    public <T extends AbstractPacket> T deserialize(Class<T> packetClass, ByteBuf buffer) throws Exception {
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        return JsonUtils.getMapper().readValue(bytes, packetClass);
    }
}
