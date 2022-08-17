package com.github.junzzzz.genericeffects.effects;

import com.github.junzzzz.genericeffects.utils.SkillUtils;
import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;
import com.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

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
