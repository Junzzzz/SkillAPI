package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class ResistanceEffect extends AbstractBilateralParamLevelPotionEffect {
    public ResistanceEffect() {
        super(Potion.resistance);
    }
}
