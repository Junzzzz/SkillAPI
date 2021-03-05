package skillapi.api.annotation;

import cpw.mods.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jun
 * @date 2020/8/20.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillEvent {
    /**
     * Only effective when inheriting {@link skillapi.event.base.BaseSkillEvent}
     *
     * @return Effective range
     */
    Side[] value() default {Side.SERVER, Side.CLIENT};
}
