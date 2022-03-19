package genericskill.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class BurningEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private int secondDuration;

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        target.setFire(secondDuration);
        return true;
    }
}
