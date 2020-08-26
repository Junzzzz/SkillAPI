package skillapi.api;

import java.lang.annotation.Annotation;

/**
 * @author Jun
 * @date 2020/8/26.
 */
public @interface SkillAnnotation {
    Class<? extends Annotation> value();
}
