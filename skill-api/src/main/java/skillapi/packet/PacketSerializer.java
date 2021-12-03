package skillapi.packet;

import io.netty.buffer.ByteBuf;

/**
 * @author Jun
 */
public interface PacketSerializer {
    byte[] serialize(AbstractPacket packet) throws Exception;

    <T extends AbstractPacket> T deserialize(Class<T> packetClass, ByteBuf buffer) throws Exception;
}
