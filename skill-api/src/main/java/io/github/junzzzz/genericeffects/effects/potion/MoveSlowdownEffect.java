package io.github.junzzzz.genericeffects.effects.potion;

import io.github.junzzzz.genericeffects.potion.GenericSkillPotions;
import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class MoveSlowdownEffect extends AbstractDynamicLevelPotionEffect {
    @SkillParam
    private int percentage;

    public MoveSlowdownEffect() {
        super(GenericSkillPotions.MOVE_SLOWDOWN);
    }

    @Override
    protected int getLevel() {
        return percentage;
    }
}
