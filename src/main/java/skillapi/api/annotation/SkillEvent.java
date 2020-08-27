package skillapi.api.annotation;

import skillapi.api.common.EffectSide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jun
 * @date 2020/8/20.
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SkillEvent {
    /**
     * Only effective when inheriting {@link skillapi.base.BaseSkillEvent}
     *
     * @return Effective range
     */
    EffectSide[] value() default {EffectSide.SERVER, EffectSide.CLIENT};
}
