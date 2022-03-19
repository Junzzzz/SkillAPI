package genericskill.effects;

import genericskill.potion.WeaponBleedPotionEffect;
import genericskill.utils.SkillUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class WeaponBleedingEffect extends WeaponDamageEffect {
    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo info) {
        double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

        // Effect addition
        damage *= this.damagePercentage;

        target.addPotionEffect(new WeaponBleedPotionEffect(player, SkillUtils.getDamage(damage)));
        return true;
    }
}
