package io.github.junzzzz.skillapi.utils;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.junzzzz.skillapi.common.SkillRuntimeException;
import io.github.junzzzz.skillapi.entity.EntityBrightReddustFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jun
 * @date 2020/9/26.
 */
@SideOnly(Side.CLIENT)
public class ClientUtils {
    private static final Minecraft MC;
    private static final Timer TIMER;

    static {
        MC = Minecraft.getMinecraft();
        try {
            Field timer = ReflectionHelper.findField(Minecraft.class, "timer", "field_71428_T");
            timer.setAccessible(true);
            TIMER = (Timer) timer.get(MC);
        } catch (Exception e) {
            throw new SkillRuntimeException("Failed to init skill", e);
        }
    }

    public static EntityLivingBase getPointedLivingEntity(double distance) {
        return getPointedLivingEntity(distance, TIMER.renderPartialTicks);
    }

    public static EntityLivingBase getPointedLivingEntity(double distance, float renderTickTime) {
        Vec3 pos = MC.renderViewEntity.getPosition(renderTickTime);
        Vec3 look = MC.renderViewEntity.getLook(renderTickTime);
        Vec3 lookPos = pos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
        Entity pointedEntity = null;
        double f1 = 1.0D;
        @SuppressWarnings("rawtypes")
        List list = MC.theWorld.getEntitiesWithinAABBExcludingEntity(MC.renderViewEntity,
                MC.renderViewEntity.boundingBox.addCoord(look.xCoord * distance, look.yCoord * distance,
                        look.zCoord * distance).expand(f1, f1, f1));
        double minDistance = distance;

        for (Object o : list) {
            Entity entity = (Entity) o;

            if (entity.canBeCollidedWith()) {
                float f2 = entity.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
                MovingObjectPosition mop = axisalignedbb.calculateIntercept(pos, lookPos);

                if (axisalignedbb.isVecInside(pos)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        pointedEntity = entity;
                        minDistance = 0.0D;
                    }
                } else if (mop != null) {
                    double d3 = pos.distanceTo(mop.hitVec);

                    if (d3 < minDistance || minDistance == 0.0D) {
                        if (entity == MC.renderViewEntity.ridingEntity && !entity.canRiderInteract()) {
                            if (minDistance == 0.0D) {
                                pointedEntity = entity;
                            }
                        } else {
                            pointedEntity = entity;
                            minDistance = d3;
                        }
                    }
                }
            }
        }

        if (pointedEntity != null) {
            if (pointedEntity instanceof EntityLivingBase) {
                return (EntityLivingBase) pointedEntity;
            }
        }
        return null;
    }

    /**
     * @param width  宽度
     * @param height 高度
     * @param length 长度
     * @return 玩家面向方向范围内的实体集合
     */
    public static List<EntityLivingBase> getPointedDirectionEntitiesByBox(double width, double height, double length) {
        EntityLivingBase player = MC.renderViewEntity;
        Vec3 position = player.getPosition(TIMER.renderPartialTicks);
        Vec3 look = MC.renderViewEntity.getLook(TIMER.renderPartialTicks);
        Set<EntityLivingBase> result = new HashSet<>(4);

        double look2DLength = Math.sqrt(look.xCoord * look.xCoord + look.zCoord * look.zCoord);
        Vector2d unitLook2D = new Vector2d(look.xCoord / look2DLength, look.zCoord / look2DLength);
        Vector2d sideA = new Vector2d(-unitLook2D.y * width / 2, unitLook2D.x * width / 2);
        Vector2d sideB = new Vector2d(unitLook2D.y * width / 2, -unitLook2D.x * width / 2);
        Vector2d v0 = new Vector2d(position.xCoord + sideA.x, position.zCoord + sideA.y);
        Vector2d v3 = new Vector2d(position.xCoord + sideB.x, position.zCoord + sideB.y);

        Vector2d v1 = new Vector2d(position.xCoord + sideA.x + unitLook2D.x * length, position.zCoord + sideA.y + unitLook2D.y * length);
        Vector2d v2 = new Vector2d(position.xCoord + sideB.x + unitLook2D.x * length, position.zCoord + sideB.y + unitLook2D.y * length);

        YAxisAlignedBoundingBox range = new YAxisAlignedBoundingBox(player.boundingBox.minY, player.boundingBox.minY + height, v0, v1, v2, v3);
        List<Entity> entities = range.searchWorldEntities(MC.theWorld);
        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase) {
                result.add((EntityLivingBase) entity);
            }
        }
        result.remove(player);

        return new ArrayList<>(result);
    }

    public static void spawnParticle(Vec3 position, Vec3 motion) {
        spawnParticle(MathUtils.tranform(position), MathUtils.tranform(motion));
    }

    public static void spawnParticle(Vector3d position, Vector3d motion) {
        MC.effectRenderer.addEffect(new EntityBrightReddustFX(MC.theWorld, position.x, position.y, position.z, motion.x, motion.y, motion.z));
    }

    public static boolean isInGame() {
        return MC.renderViewEntity != null && MC.theWorld != null;
    }

    public static EntityClientPlayerMP getPlayer() {
        return MC.thePlayer;
    }

    public static long getTotalTime() {
        return MC.theWorld.getTotalWorldTime();
    }
}
