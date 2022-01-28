package skillapi.packet.serializer;

import io.netty.buffer.ByteBuf;
import skillapi.packet.base.AbstractPacket;
import skillapi.packet.base.CallbackPacket;
import skillapi.packet.base.Packet;

/**
 * @author Jun
 */
public class DefaultPacketSerializer implements PacketSerializer<AbstractPacket> {
    private final JsonPacketSerializer json = Packet.getSerializer(JsonPacketSerializer.class);
    private final CallbackPacket.Serializer callback = Packet.getSerializer(CallbackPacket.Serializer.class);

    @Override
    public void serialize(AbstractPacket packet, ByteBuf buffer) throws Exception {
        if (packet instanceof CallbackPacket) {
            callback.serialize((CallbackPacket<?>) packet, buffer);
        } else {
            json.serialize(packet, buffer);
        }
    }

    @Override
    public AbstractPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        if (CallbackPacket.class.isAssignableFrom(packetClass)) {
            return callback.deserialize(packetClass, buffer);
        } else {
            return json.deserialize(packetClass, buffer);
        }
    }
}
