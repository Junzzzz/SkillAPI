package io.github.junzzzz.skillapi.skill;

import net.minecraft.util.ResourceLocation;

/**
 * @author Jun
 */
public final class SkillIcon {
    public static ResourceLocation valueOf(String str) {
        if (str != null) {
            int i = str.indexOf(":");
            if (i != -1) {
                return new ResourceLocation(str.substring(0, i), AbstractSkill.RESOURCE_ICON_PREFIX + str.substring(i + 1));
            }
        }
        return null;
    }

    public static String stringify(ResourceLocation icon) {
        if (icon != null) {
            String mod = icon.getResourceDomain();
            String path = icon.getResourcePath();
            int i = path.indexOf(AbstractSkill.RESOURCE_ICON_PREFIX);
            if (i != -1) {
                i += AbstractSkill.RESOURCE_ICON_PREFIX.length();
                return mod + ":" + path.substring(i);
            }
        }

        return "";
    }
}
