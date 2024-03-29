package io.github.junzzzz.skillapi.api.annotation;

import cpw.mods.fml.relauncher.Side;
import io.github.junzzzz.skillapi.event.base.AbstractSkillEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jun
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillEvent {
    /**
     * Only effective when inheriting {@link AbstractSkillEvent}
     *
     * @return Effective range
     */
    Side[] value() default {Side.SERVER, Side.CLIENT};
}
