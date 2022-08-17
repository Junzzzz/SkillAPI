package com.github.junzzzz.skillapi.api.annotation.impl;

import com.github.junzzzz.skillapi.api.annotation.SkillAnnotation;
import com.github.junzzzz.skillapi.api.annotation.SkillAnnotationRegister;
import com.github.junzzzz.skillapi.api.annotation.StaticSkill;
import com.github.junzzzz.skillapi.common.SkillRuntimeException;
import com.github.junzzzz.skillapi.skill.AbstractStaticSkill;
import com.github.junzzzz.skillapi.skill.Skills;
import com.github.junzzzz.skillapi.utils.ReflectionUtils;
import cpw.mods.fml.common.ModMetadata;

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
