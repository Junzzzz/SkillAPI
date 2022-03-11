package genericskill.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractSkillEffect;

/**
 * @author Jun
 */
@SkillEffect
public class WeaponDamageEffect extends AbstractSkillEffect {
    @SkillParam
    protected double damagePercentage;

    @Override
    public boolean canUnleash(EntityPlayer player, EntityLivingBase entity) {
        InventoryPlayer inv = player.inventory;
        ItemStack heldItem = inv.getCurrentItem();

        // Item can attack
        if (heldItem == null || !heldItem.isItemStackDamageable() || heldItem.isStackable()) {
            return false;
        }
        return entity.canAttackWithItem() && !entity.hitByEntity(player);
    }

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase entity) {
        double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

//        float enchantmentAddition = EnchantmentHelper.getEnchantmentModifierLiving(player, entity);
//        if (damage > 0 || enchantmentAddition > 0) {
//            damage += enchantmentAddition;
//        }

        // Effect addition
        damage *= this.damagePercentage;

        if (damage > 0) {


            boolean createDamage = entity.attackEntityFrom(DamageSource.causePlayerDamage(player), getDamage(damage));

            if (createDamage) {
                afterCreateDamage(player, entity);
                return true;
            }
        }

        return false;
    }

    public void afterCreateDamage(EntityPlayer player, EntityLivingBase entity) {
        player.setLastAttacker(entity);
    }

    public float getDamage(double damage) {
        damage = Math.round(damage);

        if (damage < 1.0D) {
            damage = 1.0D;
        }
        return (float) damage;
    }
}
