package genericskill.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class LifeStealingEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private float damagePercentage;

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        // Use with other skill effects
    }

    @Override
    public void afterUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (target == null) {
            return;
        }
        // Calculate target damage
        float damage = target.prevHealth - target.getHealth();
        player.heal(damage * damagePercentage);
    }
}
