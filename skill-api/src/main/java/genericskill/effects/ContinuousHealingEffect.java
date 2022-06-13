package genericskill.effects;

import genericskill.potion.GenericSkillPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;
import skillapi.skill.SkillExtraInfo;

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
    public boolean canUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        return player != null;
    }

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (self) {
            player.addPotionEffect(new PotionEffect(GenericSkillPotions.SKILL_HEAL.id, secondDuration * 20, healAmount));
        } else {
            target.addPotionEffect(new PotionEffect(GenericSkillPotions.SKILL_HEAL.id, secondDuration * 20, healAmount));
        }
        return true;
    }
}