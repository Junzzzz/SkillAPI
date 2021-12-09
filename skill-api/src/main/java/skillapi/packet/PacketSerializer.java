package skillapi.packet;

import io.netty.buffer.ByteBuf;

/**
 * @author Jun
 */
public interface PacketSerializer<T extends AbstractPacket> {
    void serialize(T packet, ByteBuf buffer) throws Exception;

    T deserialize(Class<T> packetClass, ByteBuf buffer) throws Exception;
}
