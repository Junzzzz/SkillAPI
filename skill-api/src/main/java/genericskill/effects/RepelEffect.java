package genericskill.effects;

import genericskill.utils.SkillUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
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
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo info) {
        Vec3 look = player.getLookVec();
        target.motionX += SkillUtils.getMotionXZ(look.xCoord * distance);
        target.motionY += 0.1D;
        target.motionZ += SkillUtils.getMotionXZ(look.zCoord * distance);
        target.isAirBorne = true;
    }
}
