package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class InvisibilityEffect extends AbstractBilateralDynamicLevelPotionEffect {
    public InvisibilityEffect() {
        super(Potion.invisibility);
    }

    @Override
    protected int getLevel() {
        return 0;
    }
}
