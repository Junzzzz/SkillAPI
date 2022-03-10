package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
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

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase entity) {
        for (SkillEffect effect : effects) {
            effect.unleash(player, entity);
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
