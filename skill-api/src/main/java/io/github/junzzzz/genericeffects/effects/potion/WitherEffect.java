package io.github.junzzzz.genericeffects.effects.potion;

import io.github.junzzzz.genericeffects.potion.DynamicPotionEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;

import java.util.function.BiConsumer;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class WitherEffect extends AbstractDynamicFrequencyPotionEffect {
    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        BiConsumer<DynamicPotionEffect, EntityLivingBase> trigger = (effect, entity) -> entity.attackEntityFrom(DamageSource.wither, damage);
        DynamicPotionEffect effect = new DynamicPotionEffect(Potion.wither, (int) (duration * 20), (int) (triggerFrequency * 20), trigger);
        target.addPotionEffect(effect);
    }
}
