package genericskills.effects.potion;

import genericskills.potion.GenericSkillPotions;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class MoveSpeedEffect extends AbstractBilateralDynamicLevelPotionEffect {
    @SkillParam
    private int percentage;

    public MoveSpeedEffect() {
        super(GenericSkillPotions.MOVE_SPEED);
    }

    @Override
    protected int getLevel() {
        return percentage;
    }
}
