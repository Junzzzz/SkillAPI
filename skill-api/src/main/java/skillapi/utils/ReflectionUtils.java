package skillapi.utils;

import skillapi.common.SkillRuntimeException;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jun
 */
public class ReflectionUtils {
    private static final Map<Class<?>, SoftReference<Field[]>> declaredFieldsCache = new ConcurrentHashMap<>(256);
    private static final Field[] NO_FIELDS = new Field[0];

    public static <T> T newEmptyInstance(Class<T> target, String msgFormat, Object... msgArgs) {
        try {
            Class<?>[] empty = {};
            Constructor<T> constructor = target.getDeclaredConstructor(empty);
            constructor.setAccessible(true);
            return constructor.newInstance((Object[]) null);
        } catch (Throwable e) {
            throw new SkillRuntimeException(e, msgFormat, msgArgs);
        }
    }

    public static List<Field> getAllFields(Class<?> clz) {
        List<Field> result = new ArrayList<>(clz.getDeclaredFields().length + 2);
        Class<?> current = clz;
        while (current != null) {
            Field[] declaredFields = getDeclaredFields(current);
            result.addAll(Arrays.asList(declaredFields));
            current = current.getSuperclass();
        }
        return result;
    }


    private static Field[] getDeclaredFields(Class<?> clz) {
        SoftReference<Field[]> reference = declaredFieldsCache.get(clz);
        Field[] result;
        if (reference == null || (result = reference.get()) == null) {
            result = clz.getDeclaredFields();
            declaredFieldsCache.put(clz, new SoftReference<>(result.length == 0 ? NO_FIELDS : result));
        }
        return result;
    }

    /**
     * Find which class the field is in, including super classes
     */
    public static <T> Class<? super T> findClassByFieldName(Class<T> fromClass, String name) {
        Class<? super T> result = fromClass;
        while (result != null) {
            Field[] declaredFields = getDeclaredFields(result);
            for (Field field : declaredFields) {
                if (field.getName().equals(name)) {
                    return result;
                }
            }
            result = result.getSuperclass();
        }
        return null;
    }
}
