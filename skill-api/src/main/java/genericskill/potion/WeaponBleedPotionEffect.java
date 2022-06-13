package genericskill.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

/**
 * @author Jun
 */
public class WeaponBleedPotionEffect extends PotionEffect {
    private final EntityPlayer fromPlayer;
    private final float damage;

    public WeaponBleedPotionEffect(EntityPlayer fromPlayer, int duration, float damage) {
        super(GenericSkillPotions.WEAPON_BLEED.id, duration * 20, 0);
        this.fromPlayer = fromPlayer;
        this.damage = damage;
    }

    @Override
    public boolean onUpdate(EntityLivingBase entity) {
        int duration = this.getDuration();

        if (duration % 20 == 0) {
            // Per second
            this.performEffect(entity);
        }

        // Using original function to reduce duration
        return super.onUpdate(entity);
    }

    @Override
    public void performEffect(EntityLivingBase entity) {
        if (this.getDuration() > 0) {
            entity.attackEntityFrom(DamageSource.causePlayerDamage(fromPlayer), damage);
        }
    }
}
