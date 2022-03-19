package skillapi.skill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

/**
 * @author Jun
 */
public abstract class AbstractSkillEffect implements SkillEffect {
    @Override
    public String getUnlocalizedName() {
        return "skill.effect." + Skills.getModId(getClass()) + "." + getClass().getSimpleName();
    }

    public String getParamName(String param) {
        return getUnlocalizedName() + "." + param;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        return true;
    }

    @Override
    public boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        return player != null;
    }

    @Override
    public void afterUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        // Default: none
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void clientUnleash(EntityPlayer player) {
        // Default: none
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractSkillEffect that = (AbstractSkillEffect) o;

        return Objects.equals(getUnlocalizedName(), that.getUnlocalizedName());
    }

    @Override
    public int hashCode() {
        return getUnlocalizedName().hashCode();
    }
}
