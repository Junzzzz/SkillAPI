package skillapi.skill;

import net.minecraft.util.ResourceLocation;
import skillapi.common.Translation;

/**
 * @author Jun
 */
public abstract class AbstractSkill extends AbstractSkillEffect {
    public static final String RESOURCE_ICON_PREFIX = "textures/icons/";

    protected String unlocalizedName;
    protected ResourceLocation iconResource;

    protected int mana;
    protected long cooldown;
    protected int charge;

    public final int getMana() {
        return mana;
    }

    public final long getCooldown() {
        return cooldown;
    }

    public final int getCharge() {
        return charge;
    }

    @Override
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public String getLocalizedName() {
        return Translation.format(this.unlocalizedName);
    }

    public String getDescription() {
        return Translation.format(this.unlocalizedName + ".description");
    }

    public ResourceLocation getIconResource() {
        return this.iconResource;
    }

    @Override
    public int hashCode() {
        return unlocalizedName != null ? unlocalizedName.hashCode() : 0;
    }
}
