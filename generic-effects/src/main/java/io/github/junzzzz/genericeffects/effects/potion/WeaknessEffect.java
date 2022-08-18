package io.github.junzzzz.genericeffects.effects.potion;

import io.github.junzzzz.genericeffects.potion.GenericSkillPotions;
import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class WeaknessEffect extends AbstractDynamicLevelPotionEffect {
    @SkillParam
    private int amount;

    public WeaknessEffect() {
        super(GenericSkillPotions.WEAKNESS);
    }

    @Override
    protected int getLevel() {
        return this.amount;
    }
}
