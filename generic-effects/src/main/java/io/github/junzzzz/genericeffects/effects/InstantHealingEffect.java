package io.github.junzzzz.genericeffects.effects;

import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillParam;
import io.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;
import io.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
@SkillEffect
public class InstantHealingEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private int healAmount;

    @SkillParam
    private boolean self;

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (self) {
            player.heal(healAmount);
        } else {
            target.heal(healAmount);
        }
    }
}
