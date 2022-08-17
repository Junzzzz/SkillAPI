package com.github.junzzzz.genericeffects.effects;

import com.github.junzzzz.genericeffects.potion.GenericSkillPotions;
import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;
import com.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class ContinuousAbsorptionEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private int amount;

    @SkillParam
    private int secondDuration;

    @SkillParam
    private boolean self;

    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        if (this.self) {
            return true;
        }
        return super.clientBeforeUnleash(player, extraInfo);
    }

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (self) {
            player.addPotionEffect(new PotionEffect(GenericSkillPotions.ABSORPTION.id, secondDuration * 20, amount));
        } else {
            target.addPotionEffect(new PotionEffect(GenericSkillPotions.ABSORPTION.id, secondDuration * 20, amount));
        }
    }
}
