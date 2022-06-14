package genericskill.effects.potion;

import genericskill.potion.DynamicPotionEffect;
import genericskill.potion.GenericSkillPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import skillapi.api.annotation.SkillEffect;
import skillapi.skill.SkillExtraInfo;

import java.util.function.BiConsumer;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class HarmEffect extends AbstractDynamicFrequencyPotionEffect {
    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        BiConsumer<DynamicPotionEffect, EntityLivingBase> trigger = (effect, entity) -> entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
        DynamicPotionEffect effect = new DynamicPotionEffect(GenericSkillPotions.BLEED, (int) (duration * 20), (int) (triggerFrequency * 20), trigger);
        target.addPotionEffect(effect);
        return true;
    }
}
