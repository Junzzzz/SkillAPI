package com.github.junzzzz.genericeffects.effects;

import com.github.junzzzz.genericeffects.potion.WeaponBleedPotionEffect;
import com.github.junzzzz.genericeffects.utils.SkillUtils;
import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class WeaponBleedingEffect extends WeaponDamageEffect {
    @SkillParam
    private int duration;

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo info) {
        double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

        // Effect addition
        damage *= this.damagePercentage;

        target.addPotionEffect(new WeaponBleedPotionEffect(player, duration, SkillUtils.getDamage(damage)));
    }
}
