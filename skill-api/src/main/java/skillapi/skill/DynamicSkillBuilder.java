package skillapi.skill;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.api.annotation.SkillParam;
import skillapi.api.util.Pair;
import skillapi.common.SkillRuntimeException;
import skillapi.utils.ClassUtils;
import skillapi.utils.SkillNBT;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jun
 */
public class DynamicSkillBuilder {
    @Getter
    private final int uniqueId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description = "";

    @Getter
    @Setter
    private int mana;

    @Getter
    @Setter
    private int cooldown;

    @Getter
    @Setter
    private int charge;

    private final List<Map.Entry<SkillEffect, Map<String, String>>> effects = new ArrayList<>();

    public DynamicSkillBuilder() {
        this.uniqueId = getId();
    }

    public DynamicSkillBuilder(DynamicSkillConfig config, DynamicSkill skill) {
        this.uniqueId = skill.getUniqueId();
        this.name = config.getSkillName(skill);
        this.description = config.getSkillDescription(skill);
    }

    protected DynamicSkillBuilder(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    private static synchronized int getId() {
        NBTTagCompound dynamic = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC);
        int result = dynamic.getInteger("num") + 1;
        dynamic.setInteger("num", result);
        SkillNBT.save();
        return result;
    }

    public void addEffect(Class<? extends SkillEffect> clz) {
        val annotation = clz.getDeclaredAnnotation(skillapi.api.annotation.SkillEffect.class);
        if (annotation == null) {
            throw new SkillRuntimeException("Unknown Error");
        }
        Map<String, String> paramMap = new LinkedHashMap<>(clz.getDeclaredFields().length);
        SkillEffect skillEffect = ClassUtils.newEmptyInstance(clz, "A fatal error occurred and the skill " +
                "effect could not be created.");
        for (Field field : clz.getDeclaredFields()) {
            paramMap.put(field.getName(), null);
        }
        effects.add(new Pair<>(skillEffect, paramMap));
    }

    public boolean isEmpty() {
        return effects.isEmpty();
    }

    public List<SkillEffect> getEffects() {
        return Collections.unmodifiableList(this.effects.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
    }

    public List<Map.Entry<String, String>> getParams(int index) {
        return effects.get(index).getValue().entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue() != null ? e.getValue() : ""))
                .collect(Collectors.toList());
    }

    public void setParam(int index, String paramName, String value) {
        effects.get(index).getValue().put(paramName, value);
    }

    public DynamicSkill build() {
        List<SkillEffect> result = new ArrayList<>();

        for (Map.Entry<SkillEffect, Map<String, String>> entry : effects) {
            SkillEffect skillEffect = entry.getKey();
            Class<? extends SkillEffect> clz = skillEffect.getClass();

            if (!AbstractSkillEffect.class.isAssignableFrom(clz)) {
                result.add(skillEffect);
                continue;
            }

            Map<String, String> map = entry.getValue();
            for (Field field : clz.getDeclaredFields()) {
                if (field.getAnnotation(SkillParam.class) == null) {
                    continue;
                }
                String value = map.get(field.getName());
                if (value == null) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    field.set(skillEffect, convert(field.getType(), value));
                } catch (IllegalAccessException e) {
                    throw new SkillRuntimeException("Failed to create dynamic skill.", e);
                }
            }
            result.add(skillEffect);
        }

        DynamicSkill dynamicSkill = new DynamicSkill(uniqueId, result.toArray(new SkillEffect[0]));
        dynamicSkill.mana = mana;
        dynamicSkill.cooldown = cooldown;
        dynamicSkill.charge = charge;
        return dynamicSkill;
    }

    private Object convert(Class<?> type, String value) {
        if (int.class == type || Integer.class == type) {
            return Integer.valueOf(value);
        } else if (long.class == type || Long.class == type) {
            return Long.valueOf(value);
        } else if (float.class == type || Float.class == type) {
            return Float.valueOf(value);
        } else if (double.class == type || Double.class == type) {
            return Double.valueOf(value);
        } else if (type == String.class) {
            return value;
        } else {
            throw new SkillRuntimeException("Unsupported type: %s ", type);
        }
    }
}
