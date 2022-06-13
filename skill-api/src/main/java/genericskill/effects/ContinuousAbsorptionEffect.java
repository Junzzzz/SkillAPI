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
public class ContinuousAbsorptionEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private int amount;

    @SkillParam
    private int secondDuration;

    @SkillParam
    private boolean self;

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (self) {
            player.addPotionEffect(new PotionEffect(GenericSkillPotions.ABSORPTION.id, secondDuration * 20, amount));
        } else {
            target.addPotionEffect(new PotionEffect(GenericSkillPotions.ABSORPTION.id, secondDuration * 20, amount));
        }
        return true;
    }
}
