package skillapi.skill;

import javafx.util.Pair;
import lombok.val;
import skillapi.annotation.SkillParam;
import skillapi.common.SkillLog;
import skillapi.common.SkillRuntimeException;
import skillapi.utils.ClassUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Jun
 * @date 2021/3/13.
 */
public class SkillEffectBuilder {
    private final BaseSkillEffect skillEffect;
    private final Map<String, Field> fieldMap;

    public SkillEffectBuilder(Class<? extends BaseSkillEffect> clz) {
        val declaredFields = clz.getDeclaredFields();
        this.fieldMap = new HashMap<>(declaredFields.length);
        for (Field f : declaredFields) {
            if (!f.isAnnotationPresent(SkillParam.class)) {
                continue;
            }
            if (!checkBasicType(f.getType())) {
                throw new SkillRuntimeException("This type is not supported.");
            }
            f.setAccessible(true);
            fieldMap.put(f.getName(), f);
        }

        this.skillEffect = ClassUtils.newEmptyInstance(clz, "A fatal error occurred and the skill effect could not be created.");
    }

    public void add(String name, Number value) {
        final Field field = fieldMap.get(name);
        if (field == null) {
            SkillLog.error("A serious error occurred, unable to add parameters to the skill effect. Cause variable: %s - %s", skillEffect.getClass().getName(), name);
            return;
        }
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

    public String getName() {
        return this.skillEffect.getName();
    }

    public int getParamSize() {
        return this.fieldMap.size();
    }

    public String getParam(String param) {
        return getParam(this.fieldMap.get(param));
    }

    private String getParam(Field field) {
        try {
            return field.get(this.skillEffect).toString();
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("The construction of skill effect has been completed, and you cannot continue to visit.");
        }
    }

    public List<Pair<String, String>> getParamList() {
        List<Pair<String, String>> list = new LinkedList<>();
        for (Map.Entry<String, Field> entry : this.fieldMap.entrySet()) {
            list.add(new Pair<>(entry.getKey(), getParam(entry.getValue())));
        }
        return list;
    }

    public BaseSkillEffect build() {

        return this.skillEffect;
    }

    private static boolean checkBasicType(Class<?> clz) {
        return clz.isPrimitive() && ("double".equals(clz.getName()) || "int".equals(clz.getName()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SkillEffectBuilder) {
            return ((SkillEffectBuilder) obj).skillEffect.getClass().equals(this.skillEffect.getClass());
        }
        return false;
    }
}
