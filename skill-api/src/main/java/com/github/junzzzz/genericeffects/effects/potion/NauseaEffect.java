package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class NauseaEffect extends AbstractParamLevelPotionEffect {
    public NauseaEffect() {
        super(Potion.confusion);
    }
}
