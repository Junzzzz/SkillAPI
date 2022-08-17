package io.github.junzzzz.genericeffects.effects.potion;

import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class JumpEffect extends AbstractBilateralParamLevelPotionEffect {
    public JumpEffect() {
        super(Potion.jump);
    }
}
