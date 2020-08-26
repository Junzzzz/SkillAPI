package skillapi.api;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import skillapi.utils.ClassUtils;
import skillapi.utils.ListUtils;
import skillapi.utils.ListUtils.Function;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @date 2020/8/26.
 */
public final class SkillApi {
    private static Map<Class<? extends Annotation>, SkillAnnotationRegister> annotationMap =
            new HashMap<Class<? extends Annotation>, SkillAnnotationRegister>(16);


    public static void init(FMLPreInitializationEvent event) {
        init(event, null);
    }

    public static void init(FMLPreInitializationEvent event, String packageName) {
        List<Class<?>> classes = ClassUtils.scanLocalClasses(event.getSourceFile(), packageName, true);
        System.out.println("All Class:" + classes.size());
        final List<Class<?>> remainClass = new LinkedList<Class<?>>();
        classes = ListUtils.filter(classes, new Function<Class<?>, Boolean>() {
            @Override
            public Boolean apply(Class<?> clz) {
                return clz.isAnnotationPresent(SkillAnnotation.class);
            }
        }, remainClass);
        System.out.println("Annotation Class:" + classes.size());
        registerAnnotation(classes);
        System.out.println("Remain Class:" + remainClass.size());
        registerAll(remainClass);
    }

    private static void registerAll(List<Class<?>> classes) {
        for (Class<?> clz : classes) {
            for (Annotation annotation : clz.getAnnotations()) {
                final SkillAnnotationRegister function = annotationMap.get(annotation.annotationType());
                if (function != null) {
                    function.register(clz);
                }
            }
        }
    }

    private static void registerAnnotation(List<Class<?>> classes) {
        for (Class<?> clz : classes) {
            registerAnnotation(clz);
        }
    }

    private static void registerAnnotation(Class<?> clz) {
        final SkillAnnotation annotation = clz.getAnnotation(SkillAnnotation.class);

        try {
            annotationMap.put(annotation.value(), (SkillAnnotationRegister) clz.newInstance());
        } catch (InstantiationException e) {
            FMLLog.log(Level.ERROR, e, "Unable to register %s class", clz.getName());
        } catch (IllegalAccessException e) {
            FMLLog.log(Level.ERROR, e, "Unable to register %s class", clz.getName());
        }
    }
}
