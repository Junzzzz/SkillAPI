package io.github.junzzzz.genericeffects.effects.potion;

import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class BlindnessEffect extends AbstractBilateralDynamicLevelPotionEffect {
    public BlindnessEffect() {
        super(Potion.blindness);
    }

    @Override
    protected int getLevel() {
        return 0;
    }
}
