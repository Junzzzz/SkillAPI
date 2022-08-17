package com.github.junzzzz.skillapi.skill;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.common.Message;
import com.github.junzzzz.skillapi.utils.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
@SkillEffect
public class DemoSkillEffect extends AbstractSkillEffect {
    @SkillParam
    private String message;

    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        EntityLivingBase pointedLivingEntity = ClientUtils.getPointedLivingEntity(5);
        if (pointedLivingEntity instanceof EntityPlayer) {
            extraInfo.put("target", pointedLivingEntity.getEntityId());
            return true;
        }
        return false;
    }

    @Override
    public boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        Integer targetId = extraInfo.get("target");
        Entity target = player.getEntityWorld().getEntityByID(targetId);
        if (target instanceof EntityPlayer) {
            extraInfo.replace("target", target);
            return true;
        }
        return false;
    }

    @Override
    public void unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        EntityPlayer target = extraInfo.get("target");
        Message.send(player, this.message);
    }

    @Override
    public void afterUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        // TODO
    }
}
