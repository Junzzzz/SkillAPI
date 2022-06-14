package genericskill.effects.potion;

import net.minecraft.potion.Potion;
import skillapi.api.annotation.SkillEffect;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class SaturationEffect extends AbstractBilateralDynamicLevelPotionEffect {
    public SaturationEffect() {
        super(Potion.field_76443_y);
    }

    @Override
    protected int getLevel() {
        return 0;
    }
}
