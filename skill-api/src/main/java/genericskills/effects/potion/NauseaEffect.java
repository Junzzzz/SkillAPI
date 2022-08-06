package genericskills.effects.potion;

import net.minecraft.potion.Potion;
import skillapi.api.annotation.SkillEffect;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class NauseaEffect extends AbstractParamLevelPotionEffect {
    public NauseaEffect() {
        super(Potion.confusion);
    }
}
