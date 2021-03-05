package skillapi.api.annotation.impl;

import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.SkillPacket;
import skillapi.newpacket.base.BaseSkillPacket;
import skillapi.newpacket.base.SkillPacketHandler;
import skillapi.newpacket.base.SkillPacketStructure;
import skillapi.skill.SkillRuntimeException;

/**
 * @author Jun
 * @date 2020/8/26.
 */
@SkillAnnotation
public final class SkillPacketAnnotationImpl implements SkillAnnotationRegister<SkillPacket> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, SkillPacket annotation) {
        if (!BaseSkillPacket.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill packet registration failed. Class: %s", target.getName());
        }

       SkillPacketHandler.register(target.getSimpleName(), new SkillPacketStructure((Class<? extends BaseSkillPacket>) target));
    }
}
