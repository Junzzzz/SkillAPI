package genericskill.skill.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import skillapi.api.annotation.SkillEffect;
import skillapi.skill.BaseSkillToLivingEntityEffect;

import java.util.Map;

/**
 * @author Jun
 * @date 2020/9/29.
 */
@SkillEffect
public class AttackByWeaponEffect extends BaseSkillToLivingEntityEffect {
    private double ratio;
    /**
     * @see EntityPlayer#attackTargetEntityWithCurrentItem
     */
    @Override
    protected void effect(EntityPlayer player, EntityLivingBase entity, Map<String, Object> params) {
        final float damage = (float) (player.getHeldItem().getItemDamage() / 100f * ratio);
        entity.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
    }
}
