package skillapi.skill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.common.SkillRuntimeException;
import skillapi.utils.SkillNBT;

import java.util.*;
import java.util.stream.Collectors;

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
            dynamicSkillConfig = new DynamicSkillConfig();
            dynamicSkillConfig.setName(CONFIG_NAME_DEFAULT);
            tag.setString(CONFIG_PROFILE_CURRENT, CONFIG_NAME_DEFAULT);
            SkillNBT.save();
        } else {
            dynamicSkillConfig = new DynamicSkillConfig();
            dynamicSkillConfig.setName(CONFIG_NAME_DEFAULT);
            byte[] profile = tag.getCompoundTag(TAG_DYNAMIC_PROFILES).getByteArray(current);
//            JsonUtils.getMapper().read
        }
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

    public static List<DynamicSkillBuilder> getDynamicSkillBuilders() {
        return Collections.unmodifiableList(
                dynamicSkillConfig.dynamicSkills.stream()
                        .map(e -> new DynamicSkillBuilder(dynamicSkillConfig, e)).collect(Collectors.toList())
        );
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
