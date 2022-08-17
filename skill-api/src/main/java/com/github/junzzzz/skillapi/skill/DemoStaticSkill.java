package com.github.junzzzz.skillapi.skill;

import com.github.junzzzz.skillapi.api.annotation.StaticSkill;
import com.github.junzzzz.skillapi.common.Message;
import com.github.junzzzz.skillapi.utils.ClientUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author Jun
 */
@StaticSkill
public class DemoStaticSkill extends AbstractStaticSkill {
    @Override
    protected void init(StaticSkillBuilder builder) {
        ResourceLocation icon = new ResourceLocation("com/github/junzzzz/skillapi", "textures/icons/xxx.png");
        builder.mana(1)      // 消耗魔力
                .cooldown(1) // 冷却时间
                .icon(icon); // 技能图标
    }

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
        Message.send(target, "You got hit by a skill");
        Message.send(player, "Unleash static skill");
    }

    @Override
    public void clientUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        super.clientUnleash(player, extraInfo);
    }
}
