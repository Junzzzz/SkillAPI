package com.github.junzzzz.genericeffects.effects;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;
import com.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

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
