package genericskill.effects;

import genericskill.potion.WeaponBleedPotionEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class WeaponBleedingEffect extends WeaponDamageEffect {
    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase entity) {
        double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

        // Effect addition
        damage *= this.damagePercentage;

        entity.addPotionEffect(new WeaponBleedPotionEffect(player, getDamage(damage)));
        return true;
    }
}
