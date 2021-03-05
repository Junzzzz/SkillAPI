package skillapi.api.impl;

import skillapi.api.SkillAnnotation;
import skillapi.api.SkillAnnotationRegister;
import skillapi.api.annotation.StaticSkill;

/**
 * @author Jun
 * @date 2020/9/4.
 */
@SkillAnnotation
public class StaticSkillAnnotationImpl implements SkillAnnotationRegister<StaticSkill> {
    @Override
    public void register(Class<?> target, StaticSkill annotation) {

    }
}
