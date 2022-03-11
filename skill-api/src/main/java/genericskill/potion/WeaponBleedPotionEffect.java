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

    public WeaponBleedPotionEffect(EntityPlayer fromPlayer, float damage) {
        super(SkillPotions.WEAPON_BLEED.id, 60 * 20, 0);
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
        entity.attackEntityFrom(DamageSource.causePlayerDamage(fromPlayer), damage);
    }
}
