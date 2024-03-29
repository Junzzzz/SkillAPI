package io.github.junzzzz.genericeffects.effects;

import io.github.junzzzz.genericeffects.utils.SkillUtils;
import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillParam;
import io.github.junzzzz.skillapi.skill.AbstractTargetSkillEffect;
import io.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class WeaponDamageEffect extends AbstractTargetSkillEffect {
    @SkillParam
    protected double damagePercentage;

    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        InventoryPlayer inv = player.inventory;
        ItemStack heldItem = inv.getCurrentItem();

        // Item can attack
        if (heldItem == null) {
            return false;
        }

        return super.clientBeforeUnleash(player, extraInfo);
    }


    @Override
    public boolean canUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (target == null) {
            return false;
        }

        InventoryPlayer inv = player.inventory;
        ItemStack heldItem = inv.getCurrentItem();

        // Item can attack
        if (heldItem == null) {
            return false;
        }
        return target.canAttackWithItem() && !target.hitByEntity(player);
    }

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

//        float enchantmentAddition = EnchantmentHelper.getEnchantmentModifierLiving(player, entity);
//        if (damage > 0 || enchantmentAddition > 0) {
//            damage += enchantmentAddition;
//        }

        // Effect addition
        damage *= this.damagePercentage;

        if (damage > 0) {
            boolean createDamage = target.attackEntityFrom(DamageSource.causePlayerDamage(player), SkillUtils.getDamage(damage));

            if (createDamage) {
                afterCreateDamage(player, target);
            }
        }
    }

    public void afterCreateDamage(EntityPlayer player, EntityLivingBase entity) {
        player.setLastAttacker(entity);
    }
}
