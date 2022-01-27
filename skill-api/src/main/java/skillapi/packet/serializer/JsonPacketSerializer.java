package skillapi.packet.serializer;

import io.netty.buffer.ByteBuf;
import skillapi.packet.base.AbstractPacket;
import skillapi.utils.JsonUtils;

/**
 * @author Jun
 */
public class JsonPacketSerializer implements PacketSerializer<AbstractPacket> {
    @Override
    public void serialize(AbstractPacket packet, ByteBuf buffer) throws Exception {
        buffer.writeBytes(JsonUtils.getMapper().writeValueAsBytes(packet));
    }

    @Override
    public AbstractPacket deserialize(Class<AbstractPacket> packetClass, ByteBuf buffer) throws Exception {
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        return JsonUtils.getMapper().readValue(bytes, packetClass);
    }
}
