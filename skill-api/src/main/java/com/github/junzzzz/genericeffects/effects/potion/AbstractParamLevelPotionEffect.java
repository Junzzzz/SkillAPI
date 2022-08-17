package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
@SkillEffect
public abstract class AbstractParamLevelPotionEffect extends AbstractDynamicLevelPotionEffect {
    @SkillParam
    protected int level;

    public AbstractParamLevelPotionEffect(Potion potion) {
        super(potion);
    }

    @Override
    protected int getLevel() {
        return this.level;
    }
}
