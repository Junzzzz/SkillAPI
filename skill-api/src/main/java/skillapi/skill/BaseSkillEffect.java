package skillapi.skill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.SkillApi;

import java.util.Locale;
import java.util.Map;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public abstract class BaseSkillEffect {
    private static final String SUFFIX_EFFECT = "effect";
    private final String i18n;

    public BaseSkillEffect() {
        String tempName = this.getClass().getSimpleName();
        final int i = tempName.toLowerCase(Locale.ENGLISH).lastIndexOf(SUFFIX_EFFECT);
        if (i != -1) {
            tempName = tempName.substring(0, i);
        }

        this.i18n = "skill.effect." + SkillApi.getModId(this.getClass()) + "." + tempName;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format(this.i18n);
    }

    @SideOnly(Side.CLIENT)
    public String getParamName(String param) {
        return I18n.format(this.i18n + param);
    }

    protected abstract void effect(EntityPlayer player, Map<String, Object> params);
}
