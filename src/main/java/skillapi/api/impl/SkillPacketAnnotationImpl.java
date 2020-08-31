package skillapi.api.impl;

import skillapi.api.SkillAnnotation;
import skillapi.api.SkillAnnotationRegister;
import skillapi.newpacket.base.BaseSkillPacket;
import skillapi.newpacket.base.SkillPacketHandler;
import skillapi.newpacket.base.SkillPacketStructure;
import skillapi.skill.SkillRuntimeException;

/**
 * @author Jun
 * @date 2020/8/26.
 */
@SkillAnnotation
public final class SkillPacketAnnotationImpl implements SkillAnnotationRegister<skillapi.api.annotation.SkillPacket> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, skillapi.api.annotation.SkillPacket annotation) {
        if (!BaseSkillPacket.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill packet registration failed. Class: %s", target.getName());
        }

        final String name = SkillPacketHandler.register(target.getSimpleName(), new SkillPacketStructure((Class<? extends BaseSkillPacket>) target));

        System.out.println("name: " + name);

    }
}
