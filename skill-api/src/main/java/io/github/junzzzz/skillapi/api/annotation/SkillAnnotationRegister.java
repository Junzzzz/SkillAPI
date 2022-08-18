package io.github.junzzzz.skillapi.api.annotation;

import cpw.mods.fml.common.ModMetadata;

import java.lang.annotation.Annotation;

/**
 * @author Jun
 */
public interface SkillAnnotationRegister<T extends Annotation> {
    /**
     * Provide loading methods for classes containing annotations
     *
     * @param target     Target class
     * @param annotation Annotation on the target class
     * @param mod        Mod metadata of the target class
     */
    void register(Class<?> target, T annotation, ModMetadata mod);
}
