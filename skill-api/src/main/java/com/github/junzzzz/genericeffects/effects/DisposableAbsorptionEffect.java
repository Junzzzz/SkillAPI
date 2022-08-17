package com.github.junzzzz.genericeffects.effects;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;
import com.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class DisposableAbsorptionEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private int amount;

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
            player.setAbsorptionAmount(player.getAbsorptionAmount() + this.amount);
        } else {
            target.setAbsorptionAmount(player.getAbsorptionAmount() + this.amount);
        }
    }
}
