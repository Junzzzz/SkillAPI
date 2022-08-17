package io.github.junzzzz.skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import io.github.junzzzz.skillapi.api.annotation.SkillAnnotation;
import io.github.junzzzz.skillapi.api.annotation.SkillAnnotationRegister;
import io.github.junzzzz.skillapi.api.annotation.SkillPacket;
import io.github.junzzzz.skillapi.common.SkillRuntimeException;
import io.github.junzzzz.skillapi.packet.base.AbstractPacket;
import io.github.junzzzz.skillapi.packet.base.Packet;

/**
 * @author Jun
 */
@SkillAnnotation
public final class SkillPacketAnnotationImpl implements SkillAnnotationRegister<SkillPacket> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, SkillPacket annotation, ModMetadata mod) {
        if (!AbstractPacket.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill packet registration failed. Class: %s", target.getName());
        }
        Packet.register((Class<? extends AbstractPacket>) target, annotation);
    }
}
