package skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.SkillPacket;
import skillapi.common.SkillRuntimeException;
import skillapi.packet.AbstractPacket;
import skillapi.packet.PacketHandler;

/**
 * @author Jun
 * @date 2020/8/26.
 */
@SkillAnnotation
public final class SkillPacketAnnotationImpl implements SkillAnnotationRegister<SkillPacket> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, SkillPacket annotation, ModMetadata mod) {
        if (!AbstractPacket.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill packet registration failed. Class: %s", target.getName());
        }
        PacketHandler.register((Class<? extends AbstractPacket>) target, annotation);
    }
}
