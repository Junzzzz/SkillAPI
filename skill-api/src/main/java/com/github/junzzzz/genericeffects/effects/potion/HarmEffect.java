package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.genericeffects.potion.DynamicPotionEffect;
import com.github.junzzzz.genericeffects.potion.GenericSkillPotions;
import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import java.util.function.BiConsumer;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class HarmEffect extends AbstractDynamicFrequencyPotionEffect {
    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        BiConsumer<DynamicPotionEffect, EntityLivingBase> trigger = (effect, entity) -> entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
        DynamicPotionEffect effect = new DynamicPotionEffect(GenericSkillPotions.BLEED, (int) (duration * 20), (int) (triggerFrequency * 20), trigger);
        target.addPotionEffect(effect);
    }
}
