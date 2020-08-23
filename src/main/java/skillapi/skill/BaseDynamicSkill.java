package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.Skill;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public final class BaseDynamicSkill extends BaseSkill {
    private List<SkillEffect> skillEffects;

    public BaseDynamicSkill(Collection<SkillEffect> effects) {
        this.skillEffects = new LinkedList<SkillEffect>(effects);
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return false;
    }

    @Override
    public void doSkill(EntityPlayer player) {
        for (SkillEffect effect : skillEffects) {
            effect.effect(player);
        }
    }
}
