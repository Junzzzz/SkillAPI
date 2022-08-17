package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.genericeffects.potion.GenericSkillPotions;
import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class MoveSpeedEffect extends AbstractBilateralDynamicLevelPotionEffect {
    @SkillParam
    private int percentage;

    public MoveSpeedEffect() {
        super(GenericSkillPotions.MOVE_SPEED);
    }

    @Override
    protected int getLevel() {
        return percentage;
    }
}
