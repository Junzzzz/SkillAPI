package skillapi.api.annotation.impl;

import cpw.mods.fml.common.ModMetadata;
import net.minecraft.item.Item;
import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.api.annotation.SkillItem;
import skillapi.common.SkillRuntimeException;
import skillapi.item.SkillItemLoader;
import skillapi.utils.ClassUtils;

/**
 * @author Jun
 */
@SkillAnnotation
public final class SkillItemAnnotationImpl implements SkillAnnotationRegister<SkillItem> {
    @Override
    public void register(Class<?> target, SkillItem annotation, ModMetadata mod) {
        if (!Item.class.isAssignableFrom(target)) {
            throw new SkillRuntimeException("Skill item registration failed. The skill effect class does not " +
                    "implement Item. Class: %s", target.getName());
        }
        Object o = ClassUtils.newEmptyInstance(target, "Failed to create an instance of class %s, please check " +
                "whether it contains an empty constructor", target.getName());
        SkillItemLoader.register((Item) o, annotation.value());
    }
}
