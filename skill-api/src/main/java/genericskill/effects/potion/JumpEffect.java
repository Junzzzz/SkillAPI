package genericskill.effects.potion;

import net.minecraft.potion.Potion;
import skillapi.api.annotation.SkillEffect;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class JumpEffect extends AbstractBilateralParamLevelPotionEffect {
    public JumpEffect() {
        super(Potion.jump);
    }
}
