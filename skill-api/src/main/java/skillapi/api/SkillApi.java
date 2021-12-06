package skillapi.api;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import skillapi.api.annotation.SkillAnnotation;
import skillapi.api.annotation.SkillAnnotationRegister;
import skillapi.item.SkillItemLoader;
import skillapi.packet.PacketHandler;
import skillapi.utils.ClassUtils;
import skillapi.utils.ListUtils;

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
    private static final Map<Class<? extends Annotation>, SkillAnnotationRegister<Annotation>> ANNOTATION_MAP =
            new HashMap<>(16);

    public static void preInit(FMLPreInitializationEvent event) {
        preInit(event, null);
    }

    public static void preInit(FMLPreInitializationEvent event, String packageName) {
        // Other action
        SkillItemLoader.preInit();

        // Load annotation
        List<Class<?>> classes = ClassUtils.scanLocalClasses(event.getSourceFile(), packageName, true);
        final List<Class<?>> remainClass = new LinkedList<>();
        classes = ListUtils.filter(classes, clz -> clz.isAnnotationPresent(SkillAnnotation.class), remainClass);
        registerAnnotation(classes);
        registerAll(remainClass, event.getModMetadata());
    }

    public static void init(FMLInitializationEvent event) {
        // Release memory
        ANNOTATION_MAP.clear();

        // Other action after annotation initialization
        PacketHandler.init();
    }

    private static void registerAll(List<Class<?>> classes, ModMetadata modMetadata) {
        for (Class<?> clz : classes) {
            for (Annotation annotation : clz.getAnnotations()) {
                final SkillAnnotationRegister<Annotation> function = ANNOTATION_MAP.get(annotation.annotationType());
                if (function != null) {
                    function.register(clz, annotation, modMetadata);
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
                    ANNOTATION_MAP.put((Class<? extends Annotation>) generic,
                            (SkillAnnotationRegister<Annotation>) clz.newInstance());
                    break;
                } catch (InstantiationException | IllegalAccessException e) {
                    FMLLog.log(Level.ERROR, e, "Unable to register %s class", clz.getName());
                }
            }
        }
    }
}
