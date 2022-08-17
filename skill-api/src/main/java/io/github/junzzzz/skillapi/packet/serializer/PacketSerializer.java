package io.github.junzzzz.skillapi.packet.serializer;

import io.github.junzzzz.skillapi.packet.base.AbstractPacket;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * @author Jun
 */
public interface PacketSerializer<T extends AbstractPacket> {
    void serialize(T packet, ByteBuf buffer) throws Exception;

    T deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception;

    static void writeBytes(ByteBuf buffer, byte[] bytes) {
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    static byte[] readBytes(ByteBuf buffer) {
        int length = buffer.readInt();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return bytes;
    }

    static void writeString(ByteBuf buffer, String str) {
        writeBytes(buffer, str.getBytes(StandardCharsets.UTF_8));
    }

    static String readString(ByteBuf buffer) {
        return new String(readBytes(buffer), StandardCharsets.UTF_8);
    }
}
