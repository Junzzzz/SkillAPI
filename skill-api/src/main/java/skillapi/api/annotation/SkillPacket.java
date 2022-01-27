package skillapi.api.annotation;

import skillapi.packet.base.AbstractPacket;
import skillapi.packet.serializer.JsonPacketSerializer;
import skillapi.packet.serializer.PacketSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jun
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillPacket {
    Class<? extends PacketSerializer<? extends AbstractPacket>> serializer() default JsonPacketSerializer.class;
}
