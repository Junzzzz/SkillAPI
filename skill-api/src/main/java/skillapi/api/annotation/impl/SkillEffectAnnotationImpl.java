package skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.SkillEffect;
import skillapi.common.SkillRuntimeException;
import skillapi.skill.Skills;

import java.lang.reflect.Modifier;

/**
 * @author Jun
 * @date 2020/9/4.
 */
@SkillAnnotation
public final class SkillEffectAnnotationImpl implements SkillAnnotationRegister<SkillEffect> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, SkillEffect annotation, ModMetadata mod) {
        if (!skillapi.skill.SkillEffect.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill effect registration failed. The skill effect class does not " +
                    "implement SkillEffect. Class: %s", target.getName());
        }

        Class<? extends skillapi.skill.SkillEffect> effect = (Class<? extends skillapi.skill.SkillEffect>) target;
        Skills.putModId(effect, mod.modId);

        if (!target.isInterface() && !Modifier.isAbstract(target.getModifiers())) {
            Skills.register(Skills.PREFIX_EFFECT + mod.modId + "." + effect.getSimpleName(), effect);
        }
    }
}
