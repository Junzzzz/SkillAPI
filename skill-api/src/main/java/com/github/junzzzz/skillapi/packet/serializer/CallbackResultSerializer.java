package com.github.junzzzz.skillapi.packet.serializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.junzzzz.skillapi.packet.base.CallbackResultPacket;
import com.github.junzzzz.skillapi.packet.base.ParameterizedDataType;
import com.github.junzzzz.skillapi.utils.JsonUtils;
import io.netty.buffer.ByteBuf;

/**
 * @author Jun
 */
public class CallbackResultSerializer implements PacketSerializer<CallbackResultPacket<?>> {
    @Override
    public void serialize(CallbackResultPacket<?> packet, ByteBuf buffer) throws Exception {
        ObjectMapper mapper = JsonUtils.getMapper();
        PacketSerializer.writeString(buffer, mapper.writeValueAsString(packet.getDataType()));
        PacketSerializer.writeString(buffer, mapper.writeValueAsString(packet));
    }

    @Override
    public CallbackResultPacket<?> deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        String dataTypeJson = PacketSerializer.readString(buffer);
        ObjectMapper mapper = JsonUtils.getMapper();
        ParameterizedDataType dataType = mapper.readValue(dataTypeJson, ParameterizedDataType.class);
        JavaType type = constructJavaType(mapper.getTypeFactory(), dataType);
        JavaType packetType = mapper.getTypeFactory()
                .constructParametrizedType(CallbackResultPacket.class, CallbackResultPacket.class, type);
        String json = PacketSerializer.readString(buffer);
        return mapper.readValue(json, packetType);
    }


    private JavaType constructJavaType(TypeFactory typeFactory, ParameterizedDataType type) throws Exception {
        Class<?> clz = Class.forName(type.getName());
        ParameterizedDataType[] parameterizedTypes = type.getParameterizedTypes();
        if (parameterizedTypes != null && parameterizedTypes.length != 0) {
            JavaType[] parameterTypes = new JavaType[parameterizedTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameterTypes[i] = constructJavaType(typeFactory, parameterizedTypes[i]);
            }
            return typeFactory.constructParametrizedType(clz, clz, parameterTypes);
        } else {
            return typeFactory.constructType(clz);
        }
    }
}
