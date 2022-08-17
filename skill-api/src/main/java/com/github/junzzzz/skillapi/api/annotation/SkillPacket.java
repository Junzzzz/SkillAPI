package com.github.junzzzz.skillapi.api.annotation;

import com.github.junzzzz.skillapi.packet.base.AbstractPacket;
import com.github.junzzzz.skillapi.packet.serializer.DefaultPacketSerializer;
import com.github.junzzzz.skillapi.packet.serializer.PacketSerializer;

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
    Class<? extends PacketSerializer<? extends AbstractPacket>> serializer() default DefaultPacketSerializer.class;
}
