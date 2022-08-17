package com.github.junzzzz.skillapi.api.annotation.impl;

import com.github.junzzzz.skillapi.api.annotation.SkillAnnotation;
import com.github.junzzzz.skillapi.api.annotation.SkillAnnotationRegister;
import com.github.junzzzz.skillapi.api.annotation.SkillPacket;
import com.github.junzzzz.skillapi.common.SkillRuntimeException;
import com.github.junzzzz.skillapi.packet.base.AbstractPacket;
import com.github.junzzzz.skillapi.packet.base.Packet;
import cpw.mods.fml.common.ModMetadata;

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
