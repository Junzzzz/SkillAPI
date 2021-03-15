package skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.SkillEffect;
import skillapi.common.SkillRuntimeException;
import skillapi.skill.BaseSkillEffect;
import skillapi.skill.SkillEffectHandler;

/**
 * @author Jun
 * @date 2020/9/4.
 */
@SkillAnnotation
public final class SkillEffectAnnotationImpl implements SkillAnnotationRegister<SkillEffect> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, SkillEffect annotation, ModMetadata mod) {
        if (!BaseSkillEffect.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill effect registration failed. The skill effect class does not inherit the base class BaseSkillEffect. Class: %s", target.getName());
        }
        SkillEffectHandler.register(mod.modId + "." + target.getSimpleName(), (Class<? extends BaseSkillEffect>) target);
    }
}
