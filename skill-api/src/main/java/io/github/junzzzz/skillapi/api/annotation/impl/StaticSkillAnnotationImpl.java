package io.github.junzzzz.skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import io.github.junzzzz.skillapi.api.annotation.SkillAnnotation;
import io.github.junzzzz.skillapi.api.annotation.SkillAnnotationRegister;
import io.github.junzzzz.skillapi.api.annotation.StaticSkill;
import io.github.junzzzz.skillapi.common.SkillRuntimeException;
import io.github.junzzzz.skillapi.skill.AbstractStaticSkill;
import io.github.junzzzz.skillapi.skill.Skills;
import io.github.junzzzz.skillapi.utils.ReflectionUtils;

/**
 * @author Jun
 */
@SkillAnnotation
public class StaticSkillAnnotationImpl implements SkillAnnotationRegister<StaticSkill> {
    @Override
    public void register(Class<?> target, StaticSkill annotation, ModMetadata mod) {
        if (!AbstractStaticSkill.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Static skill registration failed. The skill class does not inherit the " +
                    "base class AbstractStaticSkill. Class: %s", target.getName());
        }
        Skills.putModId(target, mod.modId);
        AbstractStaticSkill staticSkill = (AbstractStaticSkill) ReflectionUtils.newEmptyInstance(target, "Static skill " +
                "initialize failed. Class: %s", target.getName());

        Skills.register(staticSkill);
    }
}
