package io.github.junzzzz.skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import io.github.junzzzz.skillapi.api.annotation.SkillAnnotation;
import io.github.junzzzz.skillapi.api.annotation.SkillAnnotationRegister;
import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.common.SkillRuntimeException;
import io.github.junzzzz.skillapi.skill.Skills;

import java.lang.reflect.Modifier;

/**
 * @author Jun
 */
@SkillAnnotation
public final class SkillEffectAnnotationImpl implements SkillAnnotationRegister<SkillEffect> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, SkillEffect annotation, ModMetadata mod) {
        if (!io.github.junzzzz.skillapi.skill.SkillEffect.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill effect registration failed. The skill effect class does not " +
                    "implement SkillEffect. Class: %s", target.getName());
        }

        Class<? extends io.github.junzzzz.skillapi.skill.SkillEffect> effect = (Class<? extends io.github.junzzzz.skillapi.skill.SkillEffect>) target;
        Skills.putModId(effect, mod.modId);

        if (!target.isInterface() && !Modifier.isAbstract(target.getModifiers())) {
            Skills.register(Skills.PREFIX_EFFECT + mod.modId + "." + effect.getSimpleName(), effect);
        }
    }
}
