package io.github.junzzzz.genericeffects.effects.potion;

import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillParam;
import io.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;

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
