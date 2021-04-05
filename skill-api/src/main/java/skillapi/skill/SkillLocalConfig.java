package skillapi.skill;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import skillapi.common.SkillLog;
import skillapi.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @date 2020/9/4.
 */
public final class SkillLocalConfig {
    private static final String DEFAULT_CONFIG_NAME = "SkillConfig.json";

    public static SkillLocalConfig SERVER_CONFIG;

    private List<DynamicSkillConfig> customs;

    public SkillLocalConfig() {
        this.customs = new LinkedList<>();
    }

    public List<DynamicSkillConfig> getCustoms() {
        return customs;
    }

    public void setCustoms(List<DynamicSkillConfig> customs) {
        this.customs = customs;
    }

    public static SkillLocalConfig load(FMLPreInitializationEvent event) {
        SERVER_CONFIG = loadFromDir(event.getModConfigurationDirectory());
        return SERVER_CONFIG;
    }

    public static SkillLocalConfig loadFromDir(File configurationDirectory) {
        if (!configurationDirectory.isDirectory()) {
            return new SkillLocalConfig();
        }
        return loadFromFile(new File(configurationDirectory, DEFAULT_CONFIG_NAME));
    }

    public static SkillLocalConfig loadFromFile(File config) {
        if (!config.exists()) {
            return new SkillLocalConfig();
        }
        try {
            return JsonUtils.getMapper().readValue(config, SkillLocalConfig.class);
        } catch (IOException e) {
            SkillLog.error("Load skill config failed! Path:%s", e, config.getAbsolutePath());
        }
        return new SkillLocalConfig();
    }

    public static SkillLocalConfig loadFromJson(String json) {
        try {
            return JsonUtils.getMapper().readValue(json, SkillLocalConfig.class);
        } catch (IOException e) {
            SkillLog.error("Load skill config failed! Json:%s", e, json);
        }
        return new SkillLocalConfig();
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class DynamicSkillConfig {
        private String name;
        private List<SkillEffectConfig> effects;

        public DynamicSkillConfig(String name) {
            this.name = name;
            this.effects = new LinkedList<>();
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class SkillEffectConfig {
        /**
         * Effect name
         */
        private String name;
        private Map<String, Number> params;
    }
}
