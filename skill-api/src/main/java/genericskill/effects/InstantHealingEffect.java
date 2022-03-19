package genericskill.effects;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractSkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect
public class InstantHealingEffect extends AbstractSkillEffect {
    @SkillParam
    private int healAmount;

    @Override
    public boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        return player != null;
    }

    @Override
    public boolean unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        player.heal(healAmount);
        return true;
    }
}
