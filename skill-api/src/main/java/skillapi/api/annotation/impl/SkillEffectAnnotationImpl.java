package skillapi.api.annotation.impl;

import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.SkillEffect;
import skillapi.skill.BaseSkillEffect;
import skillapi.skill.SkillEffectHandler;
import skillapi.skill.SkillRuntimeException;

/**
 * @author Jun
 * @date 2020/9/4.
 */
@SkillAnnotation
public final class SkillEffectAnnotationImpl implements SkillAnnotationRegister<SkillEffect> {
    @Override
    @SuppressWarnings("unchecked")
    public void register(Class<?> target, SkillEffect annotation) {
        if (!BaseSkillEffect.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill effect registration failed. Class: %s", target.getName());
        }
        SkillEffectHandler.register(target.getSimpleName(), (Class<? extends BaseSkillEffect>) target);
    }
}
