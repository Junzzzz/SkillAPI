package io.github.junzzzz.genericeffects.effects.potion;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.junzzzz.genericeffects.potion.DynamicPotionEffect;
import io.github.junzzzz.genericeffects.potion.GenericSkillPotions;
import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillEvent;
import io.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import java.util.function.BiConsumer;

/**
 * @author Jun
 */
@SkillEvent
@SkillEffect(callSuper = true)
public class IgniteEffect extends AbstractDynamicFrequencyPotionEffect {
    private static final DamageSource damageSource = new DamageSource("onFire").setDamageBypassesArmor().setFireDamage();

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        BiConsumer<DynamicPotionEffect, EntityLivingBase> trigger = (effect, entity) -> {
            if (entity.isBurning()) {
                entity.attackEntityFrom(damageSource, damage);
            } else {
                effect.clear();
            }
        };
        target.addPotionEffect(new DynamicPotionEffect(GenericSkillPotions.IGNITE, (int) (duration * 20), (int) (triggerFrequency * 20), trigger));
        target.setFire((int) duration);
    }

    @SubscribeEvent
    public void onLivingAttackEvent(LivingAttackEvent event) {
        EntityLivingBase entity = event.entityLiving;

        if (!entity.isBurning()) {
            return;
        }
        PotionEffect effect = entity.getActivePotionEffect(GenericSkillPotions.IGNITE);
        if (!(effect instanceof DynamicPotionEffect)) {
            return;
        }

        // Cancel fire damage
        event.setCanceled(event.source == DamageSource.onFire);
    }
}
