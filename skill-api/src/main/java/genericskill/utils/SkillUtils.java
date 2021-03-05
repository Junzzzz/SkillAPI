package genericskill.utils;

import net.minecraft.client.resources.I18n;

/**
 * @author Jun
 * @date 2020/8/17.
 */
public class SkillUtils {
    public static String getSkillDescription(String skillName) {
        return I18n.format(String.format("skill.description.%s", skillName));
    }

    public static String getSkillI18nName(String skill) {
        return I18n.format(String.format("skill.name.%s", skill));
    }
}
