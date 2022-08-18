package io.github.junzzzz.skillapi.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jun
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillParam {
    /**
     * Is it a universal parameter
     * @return If {@code true}, the parameter will be set only once
     */
    boolean universal() default false;
}
