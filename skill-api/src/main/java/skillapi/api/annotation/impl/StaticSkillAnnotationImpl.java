package skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.StaticSkill;
import skillapi.common.SkillRuntimeException;
import skillapi.skill.BaseStaticSkill;
import skillapi.skill.StaticSkillManager;
import skillapi.utils.ClassUtils;

/**
 * @author Jun
 * @date 2020/9/4.
 */
@SkillAnnotation
public class StaticSkillAnnotationImpl implements SkillAnnotationRegister<StaticSkill> {
    @Override
    public void register(Class<?> target, StaticSkill annotation, ModMetadata mod) {
        if (!BaseStaticSkill.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Static skill registration failed. The skill class does not inherit the " +
                    "base class BaseStaticSkill. Class: %s", target.getName());
        }
        final BaseStaticSkill staticSkill = (BaseStaticSkill) ClassUtils.newEmptyInstance(target, "Static skill " +
                "initialize failed. Class: %s", target.getName());

        StaticSkillManager.register(staticSkill);
    }
}
