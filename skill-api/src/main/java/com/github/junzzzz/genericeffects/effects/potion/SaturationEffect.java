package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class SaturationEffect extends AbstractBilateralDynamicLevelPotionEffect {
    public SaturationEffect() {
        super(Potion.field_76443_y);
    }

    @Override
    protected int getLevel() {
        return 0;
    }
}
