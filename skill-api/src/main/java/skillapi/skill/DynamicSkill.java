package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
public class DynamicSkill extends AbstractSkill {
    private final int uniqueId;
    protected final SkillEffect[] effects;

    public DynamicSkill(int uniqueId, SkillEffect[] effects) {
        this.uniqueId = uniqueId;
        this.effects = effects;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getUnlocalizedName() {
        return Skills.PREFIX_DYNAMIC + uniqueId;
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
