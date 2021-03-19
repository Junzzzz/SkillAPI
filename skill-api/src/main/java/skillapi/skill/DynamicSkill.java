package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public final class DynamicSkill extends BaseSkill {
    private List<BaseSkillEffect> skillEffects;

    public DynamicSkill(String name, Collection<BaseSkillEffect> effects) {
        super.setName(name);
        this.setEffects(effects);
    }

    public void setEffects(Collection<BaseSkillEffect> effects) {
        this.skillEffects = new LinkedList<>(effects);
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return true;
    }

    @Override
    public void doSkill(EntityPlayer player) {
        for (BaseSkillEffect effect : skillEffects) {
            effect.effect(player);
        }
    }
}
