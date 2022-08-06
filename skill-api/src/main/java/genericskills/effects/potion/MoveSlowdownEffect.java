package genericskills.effects.potion;

import genericskills.potion.GenericSkillPotions;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class MoveSlowdownEffect extends AbstractDynamicLevelPotionEffect {
    @SkillParam
    private int percentage;

    public MoveSlowdownEffect() {
        super(GenericSkillPotions.MOVE_SLOWDOWN);
    }

    @Override
    protected int getLevel() {
        return percentage;
    }
}
