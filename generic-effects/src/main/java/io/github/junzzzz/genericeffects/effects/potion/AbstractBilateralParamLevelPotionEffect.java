package io.github.junzzzz.genericeffects.effects.potion;

import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillParam;
import io.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author Jun
 */
@SkillEffect
public abstract class AbstractBilateralParamLevelPotionEffect extends AbstractParamLevelPotionEffect {
    @SkillParam
    protected boolean self;

    public AbstractBilateralParamLevelPotionEffect(Potion potion) {
        super(potion);
    }

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
            player.addPotionEffect(new PotionEffect(this.potion.id, this.duration * 20, getLevel()));
        } else {
            target.addPotionEffect(new PotionEffect(this.potion.id, this.duration * 20, getLevel()));
        }
    }
}
