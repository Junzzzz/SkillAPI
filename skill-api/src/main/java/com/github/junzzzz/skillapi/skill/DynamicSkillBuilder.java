package com.github.junzzzz.skillapi.skill;

import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.api.util.Pair;
import com.github.junzzzz.skillapi.common.SkillLog;
import com.github.junzzzz.skillapi.common.SkillRuntimeException;
import com.github.junzzzz.skillapi.utils.ReflectionUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.util.ResourceLocation;

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

    private ResourceLocation icon;

    @Getter
    private String name = "";

    @Getter
    private String description = "";

    @Getter
    private int mana;

    @Getter
    private long cooldown;

    @Getter
    @Setter
    private int charge;

    private final String PREFIX_ICON = UniversalParam.getNamePrefix() + ".icon";
    private final String PREFIX_NAME = UniversalParam.getNamePrefix() + ".name";
    private final String PREFIX_MANA = UniversalParam.getNamePrefix() + ".mana";
    private final String PREFIX_COOLDOWN = UniversalParam.getNamePrefix() + ".cooldown";
    private final String PREFIX_DESCRIPTION = UniversalParam.getNamePrefix() + ".description";

    private final Map.Entry<SkillEffect, Map<String, DynamicSkillParam>> universalEffect = new Pair<>(UniversalParam.INSTANCE, new LinkedHashMap<>(8));
    private final List<Map.Entry<SkillEffect, Map<String, DynamicSkillParam>>> effects = new ArrayList<>();
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
        this.icon = skill.getIconResource();
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
        Map<String, DynamicSkillParam> map = this.getUniversalMap();
        map.clear();
        map.put(PREFIX_ICON, new DynamicSkillParam(ResourceLocation.class, getIconResourceStr()));
        map.put(PREFIX_NAME, new DynamicSkillParam(this.name));
        map.put(PREFIX_MANA, new DynamicSkillParam(this.mana));
        map.put(PREFIX_COOLDOWN, new DynamicSkillParam(Double.class, String.format("%.3f", this.cooldown / 1000D)));
        map.put(PREFIX_DESCRIPTION, new DynamicSkillParam(this.description));
    }

    private Map<String, DynamicSkillParam> getUniversalMap() {
        return this.universalEffect.getValue();
    }

    private void putUniversalMap(String key, String value, Class<?> clz) {
        this.universalEffect.getValue()
                .computeIfAbsent(key, e -> new DynamicSkillParam(clz))
                .setValue(value);
    }

    public ResourceLocation getIconResourceLocation() {
        return this.icon;
    }

    private String getIconResourceStr() {
        return SkillIcon.stringify(this.icon);
    }

    public void setIcon(ResourceLocation icon) {
        this.icon = icon;

        if (icon == null) {
            putUniversalMap(PREFIX_ICON, "", ResourceLocation.class);
        } else {
            putUniversalMap(PREFIX_ICON, getIconResourceStr(), ResourceLocation.class);
        }
    }

    public void setIcon(String icon) {
        this.icon = SkillIcon.valueOf(icon);
        if (this.icon == null) {
            putUniversalMap(PREFIX_ICON, "", ResourceLocation.class);
        } else {
            putUniversalMap(PREFIX_ICON, icon, ResourceLocation.class);
        }
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
        putUniversalMap(PREFIX_NAME, this.name, String.class);
    }

    public void setMana(int mana) {
        this.mana = mana;
        putUniversalMap(PREFIX_MANA, String.valueOf(this.mana), Integer.class);
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
        putUniversalMap(PREFIX_COOLDOWN, String.format("%.3f", this.cooldown / 1000D), Double.class);
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
        putUniversalMap(PREFIX_DESCRIPTION, this.description, String.class);
    }

    public static List<Field> getFields(Class<? extends SkillEffect> clz) {
        val annotation = clz.getDeclaredAnnotation(com.github.junzzzz.skillapi.api.annotation.SkillEffect.class);
        if (annotation == null) {
            throw new SkillRuntimeException("Unknown Error");
        }
        return annotation.callSuper() ? ReflectionUtils.getAllFields(clz) : Arrays.asList(clz.getDeclaredFields());
    }

    private void addUniversalParam(SkillEffect effect, Field field) {
        String name = effect instanceof AbstractSkillEffect ? ((AbstractSkillEffect) effect).getParamName(field.getName()) : field.getName();
        Map<String, DynamicSkillParam> universalMap = getUniversalMap();
        field.setAccessible(true);
        try {
            if (!universalMap.containsKey(name)) {
                Object obj = field.get(effect);
                universalMap.put(name, new DynamicSkillParam(field.getType(), obj == null ? null : obj.toString()));
            }
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("Unknown Error");
        }
    }

    private String getUniversalParam(SkillEffect effect, Field field) {
        String name = effect instanceof AbstractSkillEffect ? ((AbstractSkillEffect) effect).getParamName(field.getName()) : field.getName();
        Map<String, DynamicSkillParam> universalMap = getUniversalMap();
        return universalMap.get(name).value;
    }

    private void addEffect(SkillEffect effect) {
        Class<? extends SkillEffect> clz = effect.getClass();
        List<Field> fields = getFields(clz);

        Map<String, DynamicSkillParam> paramMap = new LinkedHashMap<>(fields.size());
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
                    paramMap.put(field.getName(), new DynamicSkillParam(field.getType(), field.get(effect).toString()));
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
        Map<String, DynamicSkillParam> paramMap = new LinkedHashMap<>(fields.size());
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
                    paramMap.putIfAbsent(field.getName(), new DynamicSkillParam(field.getType(), field.get(skillEffect).toString()));
                }
            }
        } catch (IllegalAccessException e) {
            throw new SkillRuntimeException("Unknown Error");
        }
        effects.add(new Pair<>(skillEffect, paramMap));
    }

    public void setEffects(List<Class<? extends SkillEffect>> classes) {
        Map<Class<? extends SkillEffect>, Map<String, DynamicSkillParam>> oldEffects = this.effects.stream().collect(
                HashMap::new, (map, value) -> map.put(value.getKey().getClass(), value.getValue()), HashMap::putAll
        );
        Map<String, DynamicSkillParam> oldUniversalMap = new HashMap<>(getUniversalMap());
        initUniversal();
        this.effectSet.clear();
        this.effects.clear();

        // Add new effect
        for (int i = 0; i < classes.size(); i++) {
            addEmptyEffect(classes.get(i));
            // Recover
            Map.Entry<SkillEffect, Map<String, DynamicSkillParam>> entry = this.effects.get(i);
            Map<String, DynamicSkillParam> replaceMap = oldEffects.get(entry.getKey().getClass());
            if (replaceMap != null) {
                entry.getValue().replaceAll((k, v) -> replaceMap.get(k));
            }
        }

        // Recover
        getUniversalMap().replaceAll((k, v) -> {
            DynamicSkillParam param = oldUniversalMap.get(k);
            if (param != null) {
                return param;
            }
            return v;
        });
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

    @SideOnly(Side.CLIENT)
    public List<Map.Entry<String, DynamicSkillParam>> getUniversalParams() {
        return this.universalEffect.getValue().entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    @SideOnly(Side.CLIENT)
    public List<Map.Entry<String, DynamicSkillParam>> getParams(int index) {
        if (index == -1) {
            return getUniversalParams();
        }
        if (index < 0 || index >= effects.size()) {
            return new ArrayList<>();
        }
        return effects.get(index).getValue().entrySet().stream()
                .map(e -> new Pair<>(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public void setUniversalParam(String paramName, String value) {
        if (paramName.equals(PREFIX_ICON)) {
            setIcon(value);
        } else if (paramName.equals(PREFIX_NAME)) {
            setName(value);
        } else if (paramName.equals(PREFIX_MANA)) {
            setMana(Integer.parseInt(value));
        } else if (paramName.equals(PREFIX_COOLDOWN)) {
            setCooldown((long) (Double.parseDouble(value) * 1000));
        } else if (paramName.equals(PREFIX_DESCRIPTION)) {
            setDescription(value);
        } else {
            DynamicSkillParam param = getUniversalMap().get(paramName);
            if (param != null) {
                param.setValue(value);
            } else {
                SkillLog.error("Unknown error, the skill effect parameter to be edited could not be found. ParamName: %s", paramName);
            }
        }
    }

    public void setParam(int index, String paramName, String value) {
        DynamicSkillParam param = effects.get(index).getValue().get(paramName);
        if (param != null) {
            param.setValue(value);
        } else {
            SkillLog.error("Unknown error, the skill effect parameter to be edited could not be found. ParamName: %s", paramName);
        }
    }

    public DynamicSkill build(SkillProfile profile) {
        List<SkillEffect> result = new ArrayList<>();

        for (Map.Entry<SkillEffect, Map<String, DynamicSkillParam>> entry : effects) {
            SkillEffect skillEffect = entry.getKey();
            Class<? extends SkillEffect> clz = skillEffect.getClass();

            if (!AbstractSkillEffect.class.isAssignableFrom(clz)) {
                result.add(skillEffect);
                continue;
            }

            Map<String, DynamicSkillParam> map = entry.getValue();
            for (Field field : getFields(clz)) {
                SkillParam annotation = field.getAnnotation(SkillParam.class);
                if (annotation == null) {
                    continue;
                }
                String value = annotation.universal() ? getUniversalParam(skillEffect, field) : map.get(field.getName()).value;
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
        dynamicSkill.iconResource = this.icon;
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
