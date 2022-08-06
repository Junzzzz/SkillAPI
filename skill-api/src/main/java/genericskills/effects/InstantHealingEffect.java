package genericskills.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect
public class InstantHealingEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private int healAmount;

    @SkillParam
    private boolean self;

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (self) {
            player.heal(healAmount);
        } else {
            target.heal(healAmount);
        }
    }
}
