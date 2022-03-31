package skillapi.skill;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import skillapi.api.annotation.SkillParam;
import skillapi.api.util.Pair;
import skillapi.common.SkillRuntimeException;
import skillapi.utils.ReflectionUtils;

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
    private long cooldown;

    @Getter
    @Setter
    private int charge;

    private final List<Map.Entry<SkillEffect, Map<String, String>>> effects = new ArrayList<>();
    private final Set<Class<? extends SkillEffect>> effectSet = new HashSet<>(4);

    public DynamicSkillBuilder(SkillProfile profile) {
        this.uniqueId = profile.getSkillUniqueId();
        initUniversal();
    }

    public DynamicSkillBuilder(SkillProfile config, DynamicSkill skill) {
        this.uniqueId = skill.getUniqueId();
        this.name = config.getLocalizedName(skill);
        this.description = config.getDescription(skill);
        this.mana = skill.getMana();
        this.cooldown = skill.getCooldown();
        this.charge = skill.getCharge();

        // Add effect
        initUniversal();
        for (SkillEffect effect : skill.effects) {
            addEffect(effect);
        }
    }

    private void initUniversal() {
        Map<String, String> map = new HashMap<>(8);
        Map.Entry<SkillEffect, Map<String, String>> universal = new Pair<>(UniversalParam.INSTANCE, map);
        map.put("name", this.name);
        map.put("mana", String.valueOf(this.mana));
        map.put("cooldown", String.format("%.3f", this.cooldown / 1000D));
        this.effects.add(universal);
    }

    private Map<String, String> getUniversalMap() {
        return this.effects.get(0).getValue();
    }

    protected DynamicSkillBuilder(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public static List<Field> getFields(Class<? extends SkillEffect> clz) {
        val annotation = clz.getDeclaredAnnotation(skillapi.api.annotation.SkillEffect.class);
        if (annotation == null) {
            throw new SkillRuntimeException("Unknown Error");
        }
        return annotation.callSuper() ? ReflectionUtils.getAllFields(clz) : Arrays.asList(clz.getDeclaredFields());
    }

    private void addUniversalParam(SkillEffect effect, Field field) {
        String name = effect instanceof AbstractSkillEffect ? ((AbstractSkillEffect) effect).getParamName(field.getName()) : field.getName();
        Map<String, String> universalMap = getUniversalMap();
        field.setAccessible(true);
        try {
            universalMap.put(name, field.get(effect).toString());
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("Unknown Error");
        }
    }

    public void addEffect(SkillEffect effect) {
        Class<? extends SkillEffect> clz = effect.getClass();
        List<Field> fields = getFields(clz);

        Map<String, String> paramMap = new LinkedHashMap<>(fields.size());
        try {
            for (Field field : fields) {
                SkillParam annotation = field.getAnnotation(SkillParam.class);
                if (annotation == null) {
                    continue;
                }
                field.setAccessible(true);
                if (annotation.universal()) {
                    addUniversalParam(effect, field);
                } else {
                    paramMap.put(field.getName(), field.get(effect).toString());
                }
            }
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("Unknown Error");
        }
        effects.add(new Pair<>(effect, paramMap));
    }

    public void addEmptyEffect(Class<? extends SkillEffect> clz) {
        if (!effectSet.add(clz)) {
            return;
        }
        List<Field> fields = getFields(clz);
        Map<String, String> paramMap = new LinkedHashMap<>(fields.size());
        SkillEffect skillEffect = ReflectionUtils.newEmptyInstance(clz, "A fatal error occurred and the skill " +
                "effect could not be created.");

        for (Field field : fields) {
            SkillParam annotation = field.getAnnotation(SkillParam.class);
            if (annotation == null) {
                continue;
            }
            if (annotation.universal()) {
                addUniversalParam(skillEffect, field);
            } else {
                // Remove parent class duplicated field name
                paramMap.putIfAbsent(field.getName(), null);
            }
        }
        effects.add(new Pair<>(skillEffect, paramMap));
    }

    public void setEffects(List<Class<? extends SkillEffect>> classes) {
        List<Map.Entry<SkillEffect, Map<String, String>>> newList = new ArrayList<>(classes.size());
        Set<Class<? extends SkillEffect>> newSet = new HashSet<>(classes);

        // Retain the desired effect
        this.effectSet.clear();
        for (Map.Entry<SkillEffect, Map<String, String>> effect : this.effects) {
            if (newSet.remove(effect.getKey().getClass())) {
                newList.add(effect);
                this.effectSet.add(effect.getKey().getClass());
            }
        }
        this.effects.clear();
        this.effects.addAll(newList);

        // Add new effect
        for (Class<? extends SkillEffect> clz : newSet) {
            addEmptyEffect(clz);
        }
    }

    public boolean isEmpty() {
        return effects.isEmpty();
    }

    public List<SkillEffect> getEffects() {
        return Collections.unmodifiableList(this.effects.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
    }

    public List<Map.Entry<String, String>> getParams(int index) {
        if (index < 0 || index >= effects.size()) {
            return new ArrayList<>();
        }
        return effects.get(index).getValue().entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue() != null ? e.getValue() : ""))
                .collect(Collectors.toList());
    }

    public void setParam(int index, String paramName, String value) {
        effects.get(index).getValue().put(paramName, value);
    }

    public DynamicSkill build(SkillProfile profile) {
        List<SkillEffect> result = new ArrayList<>();

        for (Map.Entry<SkillEffect, Map<String, String>> entry : effects) {
            SkillEffect skillEffect = entry.getKey();
            Class<? extends SkillEffect> clz = skillEffect.getClass();

            if (!AbstractSkillEffect.class.isAssignableFrom(clz)) {
                result.add(skillEffect);
                continue;
            }

            Map<String, String> map = entry.getValue();
            for (Field field : getFields(clz)) {
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

        DynamicSkill dynamicSkill = new DynamicSkill(profile, uniqueId, result.toArray(new SkillEffect[0]));
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
