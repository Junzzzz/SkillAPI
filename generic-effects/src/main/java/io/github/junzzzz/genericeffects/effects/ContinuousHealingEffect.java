package io.github.junzzzz.genericeffects.effects;

import io.github.junzzzz.genericeffects.potion.GenericSkillPotions;
import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillParam;
import io.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;
import io.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class ContinuousHealingEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private int healAmount;

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
            player.addPotionEffect(new PotionEffect(GenericSkillPotions.SKILL_HEAL.id, secondDuration * 20, healAmount));
        } else {
            target.addPotionEffect(new PotionEffect(GenericSkillPotions.SKILL_HEAL.id, secondDuration * 20, healAmount));
        }
    }
}