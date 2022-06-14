package genericskill.effects.potion;

import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;

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
