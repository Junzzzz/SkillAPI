package skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.StaticSkill;

/**
 * @author Jun
 * @date 2020/9/4.
 */
@SkillAnnotation
public class StaticSkillAnnotationImpl implements SkillAnnotationRegister<StaticSkill> {
    @Override
    public void register(Class<?> target, StaticSkill annotation, ModMetadata mod) {

    }
}
