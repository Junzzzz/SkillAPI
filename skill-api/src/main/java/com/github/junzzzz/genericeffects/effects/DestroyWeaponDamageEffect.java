package com.github.junzzzz.genericeffects.effects;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class DestroyWeaponDamageEffect extends WeaponDamageEffect {
    @Override
    public void afterCreateDamage(EntityPlayer player, EntityLivingBase entity) {
        super.afterCreateDamage(player, entity);

        // Destroy weapon
        player.destroyCurrentEquippedItem();
    }
}
