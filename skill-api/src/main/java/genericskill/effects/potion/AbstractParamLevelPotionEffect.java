package genericskill.effects.potion;

import net.minecraft.potion.Potion;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;

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
