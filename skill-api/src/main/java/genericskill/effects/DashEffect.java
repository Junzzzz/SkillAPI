package genericskill.effects;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import genericskill.utils.SkillUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractSkillEffect;
import skillapi.skill.SkillExtraInfo;

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

    /**
     * min max k
     * min <= motion < max
     * Distance = k * motion
     */
    private static final double[][] motionXZ = new double[][]{
            // N = 0
            {0, 0},
            // N = 1
            {0.005, 1},
            // N = 2
            {0.00915751, 1.546},
            // N = 3
            {0.016772, 1.84412},
            // N = 4
            {0.0307179, 2.00689},
            // N = 5
            {0.05626, 2.09576},
            // N = 6
            {0.10304, 2.14429},
            // N = 7
            {0.188718, 2.17078},
            // N = 8
            {0.345638, 2.18525},
            // N = 9
            {0.633037, 2.19314},
            // N = 10
            {1.15941, 2.19746},
            // N = 11
            {2.12346, 2.19981},
            // N = 12
            {3.88912, 2.2011},
            // N = 13
            {7.12292, 2.2018},
            // N = 14
            {13.0456, 2.2018},
            // N = 15
            {23.8931, 2.20239},
            // N = 16
            {43.7603, 2.20251},
            // N = 17
            {80.1471, 2.20257},
            // N = 18
            {146.79, 2.2026},
            // N = 19
            {268.845, 2.20262},
            // N = 20
            {492.391, 2.20263}
    };

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
    public boolean unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        playerDash(player);
        World world = player.getEntityWorld();

        List<Integer> targetIds = extraInfo.get(KEY_DASH_TARGETS);
        for (Integer id : targetIds) {
            EntityLivingBase target = (EntityLivingBase) world.getEntityByID(id);
            attackTarget(player, target);
        }
        return true;
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

        player.motionX += getMotionXZ(look.xCoord * distance);
        player.motionZ += getMotionXZ(look.zCoord * distance);
    }

    private double getMotionXZ(double distance) {
        double absDistance = Math.abs(distance);
        if (absDistance <= 0.005) {
            return distance;
        }
        for (double[] range : motionXZ) {
            if (absDistance < range[0]) {
                return distance / range[1];
            }
        }
        return distance / 2.20264;
    }
}
