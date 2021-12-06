package skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.StaticSkill;
import skillapi.common.SkillRuntimeException;
import skillapi.skill.AbstractStaticSkill;
import skillapi.skill.Skills;
import skillapi.utils.ClassUtils;

/**
 * @author Jun
 * @date 2020/9/4.
 */
@SkillAnnotation
public class StaticSkillAnnotationImpl implements SkillAnnotationRegister<StaticSkill> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, StaticSkill annotation, ModMetadata mod) {
        if (!AbstractStaticSkill.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Static skill registration failed. The skill class does not inherit the " +
                    "base class AbstractStaticSkill. Class: %s", target.getName());
        }
        Skills.putModId((Class<? extends AbstractStaticSkill>) target, mod.modId);
        AbstractStaticSkill staticSkill = (AbstractStaticSkill) ClassUtils.newEmptyInstance(target, "Static skill " +
                "initialize failed. Class: %s", target.getName());

        Skills.register(staticSkill);
    }
}
