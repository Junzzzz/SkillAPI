package skillapi.packet.serializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import skillapi.packet.base.CallbackResultPacket;
import skillapi.utils.JsonUtils;

/**
 * @author Jun
 */
public class CallbackResultSerializer implements PacketSerializer<CallbackResultPacket<?>> {
    @Override
    public void serialize(CallbackResultPacket<?> packet, ByteBuf buffer) throws Exception {
        String clz = packet.getData().getClass().getName();
        PacketSerializer.writeString(buffer, clz);
        PacketSerializer.writeString(buffer, JsonUtils.getMapper().writeValueAsString(packet));
    }

    @Override
    public CallbackResultPacket<?> deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        String className = PacketSerializer.readString(buffer);
        Class<?> clz = Class.forName(className);
        ObjectMapper mapper = JsonUtils.getMapper();
        JavaType type = mapper.getTypeFactory().constructParametrizedType(CallbackResultPacket.class, CallbackResultPacket.class, clz);
        String json = PacketSerializer.readString(buffer);
        return mapper.readValue(json, type);
    }
}
