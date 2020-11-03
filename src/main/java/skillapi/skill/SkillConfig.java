package skillapi.skill;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import skillapi.common.SkillLog;
import skillapi.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jun
 * @date 2020/9/4.
 */
public final class SkillConfig {
    private static final String DEFAULT_CONFIG_NAME = "SkillConfig.json";

    public static SkillConfig SERVER_CONFIG;

    private List<DynamicSkillConfig> customs;

    public SkillConfig() {
        this.customs = new LinkedList<>();
    }

    public List<DynamicSkillConfig> getCustoms() {
        return customs;
    }

    public void setCustoms(List<DynamicSkillConfig> customs) {
        this.customs = customs;
    }

    public static SkillConfig load(FMLPreInitializationEvent event) {
        SERVER_CONFIG = loadFromDir(event.getModConfigurationDirectory());
        return SERVER_CONFIG;
    }

    public static SkillConfig loadFromDir(File configurationDirectory) {
        if (!configurationDirectory.isDirectory()) {
            return new SkillConfig();
        }
        return loadFromFile(new File(configurationDirectory, DEFAULT_CONFIG_NAME));
    }

    public static SkillConfig loadFromFile(File config) {
        if (!config.exists()) {
            return new SkillConfig();
        }
        try {
            return JsonUtils.getMapper().readValue(config, SkillConfig.class);
        } catch (IOException e) {
            SkillLog.error("Load skill config failed! Path:%s", e, config.getAbsolutePath());
        }
        return new SkillConfig();
    }

    public static SkillConfig loadFromJson(String json) {
        try {
            return JsonUtils.getMapper().readValue(json, SkillConfig.class);
        } catch (IOException e) {
            SkillLog.error("Load skill config failed! Json:%s", e, json);
        }
        return new SkillConfig();
    }

    public static class DynamicSkillConfig {
        private String name;
        private List<SkillEffectConfig> effects;

        public DynamicSkillConfig() {
        }

        public DynamicSkillConfig(String name) {
            this.name = name;
            this.effects = new LinkedList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SkillEffectConfig> getEffects() {
            return effects;
        }

        public void setEffects(List<SkillEffectConfig> effects) {
            this.effects = effects;
        }
    }

    public static class SkillEffectConfig {
        private String name;
        private List<Integer> prams;

        public SkillEffectConfig() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Integer> getPrams() {
            return prams;
        }

        public void setPrams(List<Integer> prams) {
            this.prams = prams;
        }
    }
}
