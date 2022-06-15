package genericskill.effects.potion;

import net.minecraft.potion.Potion;
import skillapi.api.annotation.SkillEffect;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class InvisibilityEffect extends AbstractBilateralDynamicLevelPotionEffect {
    public InvisibilityEffect() {
        super(Potion.invisibility);
    }

    @Override
    protected int getLevel() {
        return 0;
    }
}