package skillapi.skill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
public class DynamicSkill extends AbstractSkill {
    private final int uniqueId;
    protected final SkillEffect[] effects;

    public DynamicSkill(SkillProfile profile, int uniqueId, SkillEffect[] effects) {
        this.uniqueId = uniqueId;
        this.effects = effects;
        this.name = Skills.PREFIX_DYNAMIC + profile.getName() + "." + uniqueId;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getLocalizedName() {
        return Skills.getLocalizedName(this);
    }

    @Override
    public String getDescription() {
        return Skills.getSkillDescription(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        for (SkillEffect effect : effects) {
            if (!effect.clientBeforeUnleash(player, extraInfo)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canUnleash(EntityPlayer player, SkillExtraInfo info) {
        for (SkillEffect effect : effects) {
            if (!effect.canUnleash(player, info)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        boolean flag = true;

        // Execute by order
        for (SkillEffect effect : effects) {
            if (flag) {
                // Will be interrupted by the execution result of the skill effect
                flag = effect.unleash(player, extraInfo);
            }
        }

        return flag;
    }

    @Override
    public void afterUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        for (SkillEffect effect : effects) {
            effect.afterUnleash(player, extraInfo);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void clientUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        super.clientUnleash(player, extraInfo);
        for (SkillEffect effect : effects) {
            effect.clientUnleash(player, extraInfo);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DynamicSkill that = (DynamicSkill) o;

        return uniqueId == that.uniqueId;
    }

    @Override
    public int hashCode() {
        return uniqueId;
    }
}
