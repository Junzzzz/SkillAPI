package skillapi.skill;

import com.fasterxml.jackson.core.JsonProcessingException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import skillapi.common.SkillLog;
import skillapi.common.SkillRuntimeException;
import skillapi.packet.ClientSkillInitPacket;
import skillapi.utils.SkillNBT;

import java.io.IOException;
import java.util.*;

/**
 * @author Jun
 */
public final class Skills {
    private static final String TAG_DYNAMIC_PROFILES = "profiles";
    private static final String CONFIG_PROFILE_CURRENT = "current";
    private static final String CONFIG_NAME_DEFAULT = "default";

    public static final String PREFIX_STATIC = "skill.static.";
    public static final String PREFIX_EFFECT = "skill.effect.";
    public static final String PREFIX_DYNAMIC = "skill.dynamic.";

    public static final int MAX_MANA = 20;

    private static final Map<String, Class<? extends SkillEffect>> EFFECT_MAP = new HashMap<>(16);
    private static final Map<String, AbstractSkill> SKILL_MAP = new HashMap<>(16);
    private static DynamicSkillConfig dynamicSkillConfig;

    private static final Map<Class<? extends SkillEffect>, String> MOD_ID_MAP = new HashMap<>(32);

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
        SKILL_MAP.put(skill.getUnlocalizedName(), skill);
    }

    public static synchronized void register(DynamicSkillBuilder builder) {
        dynamicSkillConfig.put(builder);
    }

    public static synchronized void register(String name, Class<? extends SkillEffect> effect) {
        EFFECT_MAP.put(name, effect);
    }

    @SideOnly(Side.CLIENT)
    public static synchronized void clientSwitchConfig(DynamicSkillConfig config) {
        dynamicSkillConfig = config;
    }

    public static synchronized void serverSwitchConfig(DynamicSkillConfig config) {
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
            SkillLog.error(e, "Failed to save dynamic skill data.");
        }
    }

    public static DynamicSkillConfig getConfigCopy() {
        return dynamicSkillConfig.copy();
    }

    public static AbstractSkill get(String unlocalizedName) {
        if (unlocalizedName.startsWith(PREFIX_STATIC)) {
            return SKILL_MAP.get(unlocalizedName);
        } else if (unlocalizedName.startsWith(PREFIX_DYNAMIC)) {
            return dynamicSkillConfig.dynamicSkills.get(unlocalizedName);
        }
        return null;
    }

    public static List<AbstractSkill> getAll() {
        return getAll(true);
    }

    public static List<AbstractSkill> getAll(boolean sort) {
        List<AbstractSkill> skills = new ArrayList<>(SKILL_MAP.values());
        if (sort) {
            List<DynamicSkill> dynamicSkills = new ArrayList<>(dynamicSkillConfig.dynamicSkills.values());
            dynamicSkills.sort(Comparator.comparingInt(DynamicSkill::getUniqueId));
            skills.addAll(dynamicSkills);
        } else {
            skills.addAll(dynamicSkillConfig.dynamicSkills.values());
        }

        return Collections.unmodifiableList(skills);
    }

    public static List<String> getEffectNames() {
        return new ArrayList<>(EFFECT_MAP.keySet());
    }

    public static Class<? extends SkillEffect> getSkillEffect(String name) {
        return EFFECT_MAP.get(name);
    }

    @SideOnly(Side.CLIENT)
    public static String getI18nName(SkillEffect skill) {
        if (skill instanceof DynamicSkill) {
            return dynamicSkillConfig.getLocalizedName((DynamicSkill) skill);
        }
        return I18n.format(skill.getUnlocalizedName());
    }


    public static String getLocalizedName(AbstractSkill skill) {
        if (skill instanceof DynamicSkill) {
            return dynamicSkillConfig.getLocalizedName((DynamicSkill) skill);
        }
        return StatCollector.translateToLocal(skill.getUnlocalizedName());
    }

    public static String getSkillDescription(DynamicSkill skill) {
        return dynamicSkillConfig.getDescription(skill);
    }

    public static void putModId(Class<? extends SkillEffect> clz, String modId) {
        MOD_ID_MAP.put(clz, modId);
    }

    public static String getModId(Class<? extends SkillEffect> clz) {
        String s = MOD_ID_MAP.get(clz);
        return s == null ? "" : s;
    }

    public static ClientSkillInitPacket getInitPacket(EntityPlayerMP player) {
        PlayerSkills properties = PlayerSkills.get(player);
        NBTTagCompound tag = new NBTTagCompound();
        properties.saveNBTData(tag);
        return new ClientSkillInitPacket(dynamicSkillConfig, tag);
    }
}
