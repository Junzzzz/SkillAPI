package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
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

    @Override
    public void afterUnleash(EntityPlayer player, EntityLivingBase entity) {
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
