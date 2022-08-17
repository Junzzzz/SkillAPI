package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.genericeffects.potion.GenericSkillPotions;
import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class DamageBoostEffect extends AbstractBilateralDynamicLevelPotionEffect {
    @SkillParam
    private int percentage;

    public DamageBoostEffect() {
        super(GenericSkillPotions.DAMAGE_BOOST);
    }

    @Override
    protected int getLevel() {
        return percentage;
    }
}
