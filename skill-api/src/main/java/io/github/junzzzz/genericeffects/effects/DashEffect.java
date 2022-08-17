package io.github.junzzzz.genericeffects.effects;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.junzzzz.genericeffects.utils.SkillUtils;
import io.github.junzzzz.skillapi.api.annotation.SkillEffect;
import io.github.junzzzz.skillapi.api.annotation.SkillParam;
import io.github.junzzzz.skillapi.skill.AbstractSkillEffect;
import io.github.junzzzz.skillapi.skill.SkillExtraInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 */
@SkillEffect
public class DashEffect extends AbstractSkillEffect {
    private static final String KEY_DASH_TARGETS = "DashTargets";

    @SkillParam
    private int distance;

    @SkillParam
    private double damagePercentage;

    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        extraInfo.put(KEY_DASH_TARGETS, vision(player));
        return true;
    }

    private List<Integer> vision(EntityPlayer player) {
        List<Integer> result = new ArrayList<>(2);

        Vec3 look = player.getLookVec();
        Vec3 pos = player.getPosition(1.0F);
        Vec3 lookPos = pos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
        AxisAlignedBB box = player.boundingBox;
        AxisAlignedBB visionBox = box.addCoord(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
        double f = 1.0;
        @SuppressWarnings("rawtypes")
        List lookEntities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, visionBox.expand(f, f, f));

        for (Object entity : lookEntities) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase attackEntity = (EntityLivingBase) entity;
                if (!attackEntity.canBeCollidedWith()) {
                    continue;
                }
                float f2 = attackEntity.getCollisionBorderSize();
                AxisAlignedBB targetBox = attackEntity.boundingBox.expand(f2, f2, f2);
                if (targetBox.isVecInside(pos)) {
                    // Looking point in entity box
                    result.add(attackEntity.getEntityId());
                } else {
                    MovingObjectPosition mop = targetBox.calculateIntercept(pos, lookPos);
                    if (mop != null && pos.distanceTo(mop.hitVec) <= distance) {
                        result.add(attackEntity.getEntityId());
                    }
                }
            }
        }

        return result;
    }


    @Override
    public boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        return player.onGround && !player.isRiding();
    }

    @Override
    public void unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        playerDash(player);
        World world = player.getEntityWorld();

        List<Integer> targetIds = extraInfo.get(KEY_DASH_TARGETS);
        for (Integer id : targetIds) {
            EntityLivingBase target = (EntityLivingBase) world.getEntityByID(id);
            attackTarget(player, target);
        }
    }

    private void attackTarget(EntityPlayer player, EntityLivingBase target) {
        double damage = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();

        // Effect addition
        damage *= this.damagePercentage;
        target.attackEntityFrom(DamageSource.causePlayerDamage(player), SkillUtils.getDamage(damage));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void clientUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        playerDash(player);
    }

    private void playerDash(EntityPlayer player) {
        Vec3 look = player.getLookVec();

        player.motionX += SkillUtils.getMotionXZ(look.xCoord * distance);
        player.motionZ += SkillUtils.getMotionXZ(look.zCoord * distance);
    }
}
