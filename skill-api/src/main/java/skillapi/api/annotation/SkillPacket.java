package skillapi.api.annotation;

import skillapi.packet.AbstractPacket;
import skillapi.packet.JsonPacketSerializer;
import skillapi.packet.PacketSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jun
 * @date 2020/8/25.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillPacket {
    Class<? extends PacketSerializer<? extends AbstractPacket>> serializer() default JsonPacketSerializer.class;
}
