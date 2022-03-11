package skillapi.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jun
 * @date 2020/8/25.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillEffect {
    String value() default "";

    /**
     * Weather to search for parameters from parent class
     */
    boolean callSuper() default false;

    // TODO repeatable
    boolean repeatable() default false;
}
