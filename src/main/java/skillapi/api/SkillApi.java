package skillapi.api;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import skillapi.utils.ClassUtils;
import skillapi.utils.ListUtils;
import skillapi.utils.ListUtils.Function;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @date 2020/8/26.
 */
public final class SkillApi {
    private static Map<Class<? extends Annotation>, SkillAnnotationRegister<Annotation>> annotationMap =
            new HashMap<Class<? extends Annotation>, SkillAnnotationRegister<Annotation>>(16);


    public static void init(FMLPreInitializationEvent event) {
        init(event, null);
    }

    public static void init(FMLPreInitializationEvent event, String packageName) {
        List<Class<?>> classes = ClassUtils.scanLocalClasses(event.getSourceFile(), packageName, true);
        final List<Class<?>> remainClass = new LinkedList<Class<?>>();
        classes = ListUtils.filter(classes, new Function<Class<?>, Boolean>() {
            @Override
            public Boolean apply(Class<?> clz) {
                return clz.isAnnotationPresent(SkillAnnotation.class);
            }
        }, remainClass);
        registerAnnotation(classes);
        registerAll(remainClass);
    }

    private static void registerAll(List<Class<?>> classes) {
        for (Class<?> clz : classes) {
            for (Annotation annotation : clz.getAnnotations()) {
                final SkillAnnotationRegister<Annotation> function = annotationMap.get(annotation.annotationType());
                if (function != null) {
                    function.register(clz, annotation);
                }
            }
        }
    }

    private static void registerAnnotation(List<Class<?>> classes) {
        for (Class<?> clz : classes) {
            registerAnnotation(clz);
        }
    }

    @SuppressWarnings("unchecked")
    private static void registerAnnotation(Class<?> clz) {
        if (!SkillAnnotationRegister.class.isAssignableFrom(clz)) {
            return;
        }

        final Type[] genericInterfaces = clz.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (!(genericInterface instanceof ParameterizedType)) {
                continue;
            }

            final Type[] generics = ((ParameterizedType) genericInterface).getActualTypeArguments();
            if (generics.length != 1) {
                continue;
            }

            final Class<?> generic = (Class<?>) generics[0];
            if (Annotation.class.isAssignableFrom(generic)) {
                try {
                    annotationMap.put((Class<? extends Annotation>) generic, (SkillAnnotationRegister<Annotation>) clz.newInstance());
                    break;
                } catch (InstantiationException e) {
                    FMLLog.log(Level.ERROR, e, "Unable to register %s class", clz.getName());
                } catch (IllegalAccessException e) {
                    FMLLog.log(Level.ERROR, e, "Unable to register %s class", clz.getName());
                }
            }
        }
    }
}
