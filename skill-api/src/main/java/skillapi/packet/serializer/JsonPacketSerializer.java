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
        PacketSerializer.writeString(buffer, JsonUtils.getMapper().writeValueAsString(packet));
    }

    @Override
    public AbstractPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        return (AbstractPacket) JsonUtils.getMapper().readValue(PacketSerializer.readString(buffer), packetClass);
    }
}
