package genericskill.effects.potion;

import genericskill.potion.GenericSkillPotions;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class DamageBoostEffect extends AbstractBilateralDynamicLevelPotionEffect {
    @SkillParam
    private int percentage;

    public DamageBoostEffect() {
        super(GenericSkillPotions.DAMAGE_BOOST);
    }

    @Override
    protected int getLevel() {
        return percentage;
    }
}
