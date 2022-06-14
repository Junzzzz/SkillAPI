package genericskill.effects.potion;

import genericskill.potion.GenericSkillPotions;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class WeaknessEffect extends AbstractDynamicLevelPotionEffect {
    @SkillParam
    private int amount;

    public WeaknessEffect() {
        super(GenericSkillPotions.WEAKNESS);
    }

    @Override
    protected int getLevel() {
        return this.amount;
    }
}
