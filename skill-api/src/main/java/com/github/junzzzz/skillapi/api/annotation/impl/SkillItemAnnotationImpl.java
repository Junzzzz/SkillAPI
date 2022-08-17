package com.github.junzzzz.skillapi.api.annotation.impl;

import com.github.junzzzz.skillapi.api.annotation.SkillAnnotation;
import com.github.junzzzz.skillapi.api.annotation.SkillAnnotationRegister;
import com.github.junzzzz.skillapi.api.annotation.SkillItem;
import com.github.junzzzz.skillapi.common.SkillRuntimeException;
import com.github.junzzzz.skillapi.item.SkillItemLoader;
import com.github.junzzzz.skillapi.utils.ReflectionUtils;
import cpw.mods.fml.common.ModMetadata;
import net.minecraft.item.Item;

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
        Object o = ReflectionUtils.newEmptyInstance(target, "Failed to create an instance of class %s, please check " +
                "whether it contains an empty constructor", target.getName());
        SkillItemLoader.register((Item) o, annotation.value());
    }
}
