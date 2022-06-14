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
import java.util.stream.Stream;

/**
 * @author Jun
 */
public class DynamicSkillBuilder {
    @Getter
    private final int uniqueId;

    @Getter
    private String name;

    @Getter
    @Setter
    private String description = "";

    @Getter
    private int mana;

    @Getter
    private long cooldown;

    @Getter
    @Setter
    private int charge;

    private final String PREFIX_NAME = UniversalParam.getNamePrefix() + ".name";
    private final String PREFIX_MANA = UniversalParam.getNamePrefix() + ".mana";
    private final String PREFIX_COOLDOWN = UniversalParam.getNamePrefix() + ".cooldown";

    private final Map.Entry<SkillEffect, Map<String, String>> universalEffect = new Pair<>(UniversalParam.INSTANCE, new LinkedHashMap<>(8));
    private final List<Map.Entry<SkillEffect, Map<String, String>>> effects = new ArrayList<>();
    private final Set<Class<? extends SkillEffect>> effectSet = new HashSet<>(4);

    public DynamicSkillBuilder(SkillProfile profile) {
        this.uniqueId = profile.getSkillUniqueId();
        initUniversal();
    }

    protected DynamicSkillBuilder(int uniqueId) {
        this.uniqueId = uniqueId;
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
        Map<String, String> map = this.getUniversalMap();
        map.clear();
        map.put(PREFIX_NAME, this.name);
        map.put(PREFIX_MANA, String.valueOf(this.mana));
        map.put(PREFIX_COOLDOWN, String.format("%.3f", this.cooldown / 1000D));
    }

    private Map<String, String> getUniversalMap() {
        return this.universalEffect.getValue();
    }

    public void setName(String name) {
        this.name = name;
        this.getUniversalMap().put(PREFIX_NAME, this.name);
    }

    public void setMana(int mana) {
        this.mana = mana;
        this.getUniversalMap().put(PREFIX_MANA, String.valueOf(this.mana));
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
        this.getUniversalMap().put(PREFIX_COOLDOWN, String.format("%.3f", this.cooldown / 1000D));
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
            if (!universalMap.containsKey(name)) {
                Object obj = field.get(effect);
                universalMap.put(name, obj == null ? null : obj.toString());
            }
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("Unknown Error");
        }
    }

    private String getUniversalParam(SkillEffect effect, Field field) {
        String name = effect instanceof AbstractSkillEffect ? ((AbstractSkillEffect) effect).getParamName(field.getName()) : field.getName();
        Map<String, String> universalMap = getUniversalMap();
        return universalMap.get(name);
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
                if (annotation.universal()) {
                    addUniversalParam(effect, field);
                } else {
                    field.setAccessible(true);
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

        try {
            for (Field field : fields) {
                SkillParam annotation = field.getAnnotation(SkillParam.class);
                if (annotation == null) {
                    continue;
                }
                if (annotation.universal()) {
                    addUniversalParam(skillEffect, field);
                } else {
                    // Remove parent class duplicated field name
                    field.setAccessible(true);
                    paramMap.putIfAbsent(field.getName(), field.get(skillEffect).toString());
                }
            }
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("Unknown Error");
        }
        effects.add(new Pair<>(skillEffect, paramMap));
    }

    public void setEffects(List<Class<? extends SkillEffect>> classes) {
        Map<Class<? extends SkillEffect>, Map<String, String>> oldEffects = this.effects.stream().collect(
                HashMap::new, (map, value) -> map.put(value.getKey().getClass(), value.getValue()), HashMap::putAll
        );
        Map<String, String> oldUniversalMap = new HashMap<>(getUniversalMap());
        initUniversal();
        this.effects.clear();

        // Add new effect
        for (int i = 0; i < classes.size(); i++) {
            addEmptyEffect(classes.get(i));
            // Recover
            Map.Entry<SkillEffect, Map<String, String>> entry = this.effects.get(i);
            Map<String, String> replaceMap = oldEffects.get(entry.getKey().getClass());
            if (replaceMap != null) {
                entry.getValue().replaceAll((k, v) -> replaceMap.get(k));
            }
        }

        // Recover
        getUniversalMap().replaceAll((k, v) -> oldUniversalMap.get(k));
    }

    public boolean isEmpty() {
        return effects.isEmpty();
    }

    public List<SkillEffect> getEffectsWithUniversal() {
        List<SkillEffect> result = Stream.concat(Stream.of(this.universalEffect), this.effects.stream())
                .map(Map.Entry::getKey).collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    public List<SkillEffect> getEffects() {
        return Collections.unmodifiableList(this.effects.stream().map(Map.Entry::getKey).collect(Collectors.toList()));
    }

    public List<Map.Entry<String, String>> getUniversalParams() {
        return this.universalEffect.getValue().entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue() != null ? e.getValue() : ""))
                .collect(Collectors.toList());
    }

    public List<Map.Entry<String, String>> getParams(int index) {
        if (index == -1) {
            return getUniversalParams();
        }
        if (index < 0 || index >= effects.size()) {
            return new ArrayList<>();
        }
        return effects.get(index).getValue().entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue() != null ? e.getValue() : ""))
                .collect(Collectors.toList());
    }

    public void setUniversalParam(String paramName, String value) {
        if (paramName.equals(PREFIX_NAME)) {
            setName(value);
        } else if (paramName.equals(PREFIX_MANA)) {
            setMana(Integer.parseInt(value));
        } else if (paramName.equals(PREFIX_COOLDOWN)) {
            setCooldown((long) (Double.parseDouble(value) * 1000));
        } else {
            getUniversalMap().put(paramName, value);
        }
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
                SkillParam annotation = field.getAnnotation(SkillParam.class);
                if (annotation == null) {
                    continue;
                }
                String value = annotation.universal() ? getUniversalParam(skillEffect, field) : map.get(field.getName());
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
        } else if (boolean.class == type || Boolean.class == type) {
            return Boolean.valueOf(value);
        } else if (type == String.class) {
            return value;
        } else {
            throw new SkillRuntimeException("Unsupported type: %s ", type);
        }
    }
}
