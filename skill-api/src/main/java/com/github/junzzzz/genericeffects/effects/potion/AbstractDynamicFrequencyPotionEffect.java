package com.github.junzzzz.genericeffects.effects.potion;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;

/**
 * @author Jun
 */
@SkillEffect
public abstract class AbstractDynamicFrequencyPotionEffect extends AbstractTargetSkillEffect {
    @SkillParam
    protected int damage;

    @SkillParam
    protected float duration;

    @SkillParam
    protected float triggerFrequency;
}
