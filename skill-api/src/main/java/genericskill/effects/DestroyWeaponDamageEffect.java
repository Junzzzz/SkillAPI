package genericskill.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;

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
