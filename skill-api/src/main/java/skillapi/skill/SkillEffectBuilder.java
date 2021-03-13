package skillapi.skill;

import lombok.val;
import skillapi.common.SkillRuntimeException;
import skillapi.utils.ClassUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jun
 * @date 2021/3/13.
 */
public class SkillEffectBuilder {
    final BaseSkillEffect skillEffect;
    final Map<String, Field> fieldMap;

    public SkillEffectBuilder(BaseSkillEffect effect) {
        val clz = effect.getClass();
        val declaredFields = clz.getDeclaredFields();
        this.fieldMap = new HashMap<>(declaredFields.length);
        for (Field f : declaredFields) {
            if (!checkBasicType(f.getType())) {
                throw new SkillRuntimeException("This type is not supported.");
            }
            f.setAccessible(true);
            fieldMap.put(f.getName(), f);
        }

        this.skillEffect = ClassUtils.newEmptyInstance(clz, "A fatal error occurred and the skill effect could not be created.");
    }

    public void add(String name, Object value) {
        final Field field = fieldMap.get(name);
        try {
            if (value instanceof Double) {
                field.setDouble(skillEffect, (double) value);
            } else if (value instanceof Integer) {
                field.setInt(skillEffect, (int) value);
            } else {
                throw new SkillRuntimeException("This type is not supported.");
            }
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("A fatal error occurred.");
        }

    }

    public BaseSkillEffect build() {
        return this.skillEffect;
    }

    private static boolean checkBasicType(Class<?> clz) {
        return clz.isPrimitive() && ("double".equals(clz.getName()) || "int".equals(clz.getName()));
    }
}
