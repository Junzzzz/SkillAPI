package genericskills.effects.potion;

import net.minecraft.potion.Potion;
import skillapi.api.annotation.SkillEffect;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class NightVisionEffect extends AbstractBilateralDynamicLevelPotionEffect {
    public NightVisionEffect() {
        super(Potion.nightVision);
    }

    @Override
    protected int getLevel() {
        return 0;
    }
}
