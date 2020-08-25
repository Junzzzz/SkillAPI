package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public final class BaseDynamicSkill extends BaseSkill {
    private List<SkillEffect> skillEffects;

    public BaseDynamicSkill(String name, Collection<SkillEffect> effects) {
        super.setName(name);
        this.skillEffects = new LinkedList<SkillEffect>(effects);
    }

    public void setEffects(Collection<SkillEffect> effects) {
        this.skillEffects = new LinkedList<SkillEffect>(effects);
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return true;
    }

    @Override
    public void doSkill(EntityPlayer player) {
        for (SkillEffect effect : skillEffects) {
            effect.effect(player);
        }
    }
}
