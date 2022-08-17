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
