package skillapi.skill;

import com.fasterxml.jackson.core.JsonProcessingException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import skillapi.common.SkillLog;
import skillapi.common.SkillNBT;
import skillapi.common.SkillRuntimeException;
import skillapi.packet.ClientSkillInitPacket;
import skillapi.skill.SkillProfile.SkillProfileInfo;

import java.io.IOException;
import java.util.*;

/**
 * @author Jun
 */
public final class Skills {
    public static final String TAG_DYNAMIC_PROFILES = "profiles";
    public static final String CONFIG_PROFILE_CURRENT = "current";
    public static final String CONFIG_NAME_DEFAULT = "default";

    public static final String PREFIX_STATIC = "skill.static.";
    public static final String PREFIX_EFFECT = "skill.effect.";
    public static final String PREFIX_DYNAMIC = "skill.dynamic.";

    public static final int MAX_MANA = 20;

    private static final Map<String, Class<? extends SkillEffect>> EFFECT_MAP = new HashMap<>(16);
    private static final Map<String, AbstractSkill> SKILL_MAP = new HashMap<>(16);

    private static final SkillProfileManager profileManager = new SkillProfileManager();
    private static SkillProfile currentProfile;

    private static final Map<Class<?>, String> MOD_ID_MAP = new HashMap<>(32);

    public static synchronized void init() {
        // Init dynamic skill
        NBTTagCompound tag = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC);
        String current = tag.getString(CONFIG_PROFILE_CURRENT);
        if (current.isEmpty()) {
            tag.setString(CONFIG_PROFILE_CURRENT, CONFIG_NAME_DEFAULT);
            initDefault();

            // Init Manager
            profileManager.init(tag.getCompoundTag(TAG_DYNAMIC_PROFILES));
        } else {
            NBTTagCompound profilesTag = tag.getCompoundTag(TAG_DYNAMIC_PROFILES);

            // Load current profile
            byte[] profile = profilesTag.getByteArray(current);

            // Load empty config
            if (profile.length == 0) {
                initDefault();
                return;
            }

            try {
                currentProfile = SkillProfile.valueOf(profile);
            } catch (IOException e) {
                SkillLog.warn("Failed to load dynamic skill config. Loading default config.");
                initDefault();
            }

            // Init Manager
            profileManager.init(profilesTag);
        }
    }

    private static void initDefault() {
        SkillProfile defaultProfile = new SkillProfile();
        defaultProfile.name = CONFIG_NAME_DEFAULT;
        defaultProfile.lastUpdater = "SkillApi";
        defaultProfile.lastUpdateTime = System.currentTimeMillis();
        serverSwitchConfig(defaultProfile);
    }

    public static synchronized void register(AbstractSkill skill) {
        if (skill instanceof DynamicSkill) {
            throw new SkillRuntimeException("Please use DynamicSkillBuilder");
        }
        SKILL_MAP.put(skill.getUnlocalizedName(), skill);
    }

    public static synchronized void register(DynamicSkillBuilder builder) {
        currentProfile.put(builder);
    }

    public static synchronized void register(String name, Class<? extends SkillEffect> effect) {
        EFFECT_MAP.put(name, effect);
    }

    @SideOnly(Side.CLIENT)
    public static synchronized void clientSwitchConfig(SkillProfile profile) {
        currentProfile = profile;
    }

    public static synchronized void serverSwitchConfig(SkillProfile profile) {
        SkillProfile tmp = currentProfile;

        currentProfile = profile;
        NBTTagCompound tag = SkillNBT.getTag(SkillNBT.TAG_DYNAMIC);
        tag.setString(CONFIG_PROFILE_CURRENT, profile.name);
        try {
            NBTTagCompound profileTag = tag.getCompoundTag(TAG_DYNAMIC_PROFILES);
            tag.setTag(TAG_DYNAMIC_PROFILES, profileTag);
            profileTag.setByteArray(profile.name, profile.getBytes());
            SkillNBT.save();
        } catch (JsonProcessingException e) {
            // recover
            currentProfile = tmp;
            SkillLog.error(e, "Failed to save dynamic skill data.");
        }
    }

    public static AbstractSkill get(String unlocalizedName) {
        if (unlocalizedName.startsWith(PREFIX_STATIC)) {
            return SKILL_MAP.get(unlocalizedName);
        } else if (unlocalizedName.startsWith(PREFIX_DYNAMIC)) {
            return currentProfile.dynamicSkills.get(unlocalizedName);
        }
        return null;
    }

    public static List<AbstractSkill> getAll() {
        return getAll(true);
    }

    public static List<AbstractSkill> getAll(boolean sort) {
        List<AbstractSkill> skills = new ArrayList<>(SKILL_MAP.values());
        if (sort) {
            List<DynamicSkill> dynamicSkills = new ArrayList<>(currentProfile.dynamicSkills.values());
            dynamicSkills.sort(Comparator.comparingInt(DynamicSkill::getUniqueId));
            skills.addAll(dynamicSkills);
        } else {
            skills.addAll(currentProfile.dynamicSkills.values());
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
            return currentProfile.getLocalizedName((DynamicSkill) skill);
        }
        return I18n.format(skill.getUnlocalizedName());
    }


    public static String getLocalizedName(AbstractSkill skill) {
        if (skill instanceof DynamicSkill) {
            return currentProfile.getLocalizedName((DynamicSkill) skill);
        }
        return StatCollector.translateToLocal(skill.getUnlocalizedName());
    }

    public static String getSkillDescription(DynamicSkill skill) {
        return currentProfile.getDescription(skill);
    }

    public static void putModId(Class<?> clz, String modId) {
        if (SkillEffect.class.isAssignableFrom(clz)) {
            MOD_ID_MAP.put(clz, modId);
        }
    }

    public static String getModId(Class<? extends SkillEffect> clz) {
        String s = MOD_ID_MAP.get(clz);
        return s == null ? "" : s;
    }

    public static ClientSkillInitPacket getInitPacket(EntityPlayerMP player) {
        PlayerSkills properties = PlayerSkills.get(player);
        NBTTagCompound tag = new NBTTagCompound();
        properties.saveNBTData(tag);
        return new ClientSkillInitPacket(currentProfile, tag);
    }

    public static SkillProfileManager getProfileManager() {
        return profileManager;
    }

    public static SkillProfileInfo getCurrentProfileInfo() {
        return currentProfile.getInfo();
    }

    public static SkillProfile getProfile(String name) {
        return profileManager.get(name);
    }
}
