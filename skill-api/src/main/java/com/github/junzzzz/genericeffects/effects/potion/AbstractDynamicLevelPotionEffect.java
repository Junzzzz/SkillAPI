package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;
import com.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author Jun
 */
@SkillEffect
public abstract class AbstractDynamicLevelPotionEffect extends AbstractTargetSkillEffect {
    @SkillParam
    protected int duration;

    protected final Potion potion;

    public AbstractDynamicLevelPotionEffect(Potion potion) {
        this.potion = potion;
    }

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        target.addPotionEffect(new PotionEffect(this.potion.id, this.duration * 20, getLevel()));
    }

    protected abstract int getLevel();
}
