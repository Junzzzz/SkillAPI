package skillapi.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import skillapi.common.SkillRuntimeException;

import java.lang.reflect.Field;
import java.util.List;

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
            Field timer = MC.getClass().getDeclaredField("timer");
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
