package skillapi.skill;

import com.fasterxml.jackson.core.JsonProcessingException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.common.SkillLog;
import skillapi.common.SkillRuntimeException;
import skillapi.utils.SkillNBT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 */
public final class Skills {
    private static final String TAG_DYNAMIC_PROFILES = "profiles";
    private static final String CONFIG_PROFILE_CURRENT = "current";
    private static final String CONFIG_NAME_DEFAULT = "default";

    private static final Map<String, Class<? extends SkillEffect>> effectMap = new HashMap<>(16);
    private static final Map<String, AbstractSkill> skillMap = new HashMap<>(16);
    private static DynamicSkillConfig dynamicSkillConfig;

    public static synchronized void init() {
        // Init dynamic skill
        NBTTagCompound tag = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC);
        String current = tag.getString(CONFIG_PROFILE_CURRENT);
        if (current.isEmpty()) {
            initDefault();
            tag.setString(CONFIG_PROFILE_CURRENT, CONFIG_NAME_DEFAULT);
            SkillNBT.save();
        } else {
            byte[] profile = tag.getCompoundTag(TAG_DYNAMIC_PROFILES).getByteArray(current);

            // Load empty config
            if (profile.length == 0) {
                initDefault();
                return;
            }

            try {
                dynamicSkillConfig = DynamicSkillConfig.valueOf(profile);
            } catch (IOException e) {
                SkillLog.warn("Failed to load dynamic skill config. Loading default config.");
                initDefault();
            }
        }
    }

    private static void initDefault() {
        dynamicSkillConfig = new DynamicSkillConfig();
        dynamicSkillConfig.setName(CONFIG_NAME_DEFAULT);
    }

    public static synchronized void register(AbstractSkill skill) {
        if (skill instanceof DynamicSkill) {
            throw new SkillRuntimeException("Please use DynamicSkillBuilder");
        }
        skillMap.put(skill.getName(), skill);
    }

    public static synchronized void register(DynamicSkillBuilder builder) {
        dynamicSkillConfig.put(builder);
    }

    public static synchronized void register(String name, Class<? extends SkillEffect> effect) {
        effectMap.put(name, effect);
    }

    public static synchronized void switchConfig(DynamicSkillConfig config) {
        DynamicSkillConfig tmp = dynamicSkillConfig;

        dynamicSkillConfig = config;
        NBTTagCompound tag = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC);
        tag.setString(CONFIG_PROFILE_CURRENT, config.name);
        try {
            NBTTagCompound profile = tag.getCompoundTag(TAG_DYNAMIC_PROFILES);
            tag.setTag(TAG_DYNAMIC_PROFILES, profile);
            profile.setByteArray(config.name, config.getBytes());
            SkillNBT.save();
        } catch (JsonProcessingException e) {
            // recover
            dynamicSkillConfig = tmp;
            SkillLog.error("Failed to save dynamic skill data.", e);
        }
    }

    public static DynamicSkillConfig getConfigCopy() {
        return dynamicSkillConfig.copy();
    }

    public static List<String> getEffectNames() {
        return new ArrayList<>(effectMap.keySet());
    }

    public static Class<? extends SkillEffect> getSkillEffect(String name) {
        return effectMap.get(name);
    }

    @SideOnly(Side.CLIENT)
    public static String getI18nName(SkillEffect skill) {
        if (skill instanceof DynamicSkill) {
            return dynamicSkillConfig.getSkillName((DynamicSkill) skill);
        }
        return I18n.format(skill.getName());
    }

    public static String getSkillName(DynamicSkill skill) {
        return dynamicSkillConfig.getSkillName(skill);
    }

    public static String getSkillDescription(DynamicSkill skill) {
        return dynamicSkillConfig.getSkillDescription(skill);
    }
}
