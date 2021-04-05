package genericskill.skill.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.BaseSkillToLivingEntityEffect;

/**
 * @author Jun
 * @date 2020/9/29.
 */
@SkillEffect
public class AttackByWeaponEffect extends BaseSkillToLivingEntityEffect {
    @SkillParam
    private double ratio;

    /**
     * @see EntityPlayer#attackTargetEntityWithCurrentItem
     */
    @Override
    protected void effect(EntityPlayer player, EntityLivingBase entity) {
        final float damage = (float) (player.getHeldItem().getItemDamage() / 100f * ratio);
        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
    }
}
