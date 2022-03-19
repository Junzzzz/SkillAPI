package genericskill.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class RepelEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private double distance;

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo info) {
        target.motionX += -MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * distance;
        target.motionY += 0.1D;
        target.motionZ += MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * distance;
        target.isAirBorne = true;
        return true;
    }
}
