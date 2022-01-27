package skillapi.packet.serializer;

import io.netty.buffer.ByteBuf;
import skillapi.packet.base.AbstractPacket;

/**
 * @author Jun
 */
public interface PacketSerializer<T extends AbstractPacket> {
    void serialize(T packet, ByteBuf buffer) throws Exception;

    T deserialize(Class<T> packetClass, ByteBuf buffer) throws Exception;
}
