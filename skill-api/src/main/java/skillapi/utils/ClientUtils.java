package skillapi.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;

/**
 * @author Jun
 * @date 2020/9/26.
 */
@SideOnly(Side.CLIENT)
public class ClientUtils {
    private static final Minecraft MC = Minecraft.getMinecraft();

    public static EntityLivingBase getPointedLivingEntity(double distance, float renderTickTime) {
        Vec3 vec3 = MC.renderViewEntity.getPosition(renderTickTime);
        Vec3 vec31 = MC.renderViewEntity.getLook(renderTickTime);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
        Entity pointedEntity = null;
        double f1 = 1.0D;
        List list = MC.theWorld.getEntitiesWithinAABBExcludingEntity(MC.renderViewEntity, MC.renderViewEntity.boundingBox.addCoord(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance).expand(f1, f1, f1));
        double minDistance = distance;

        for (Object o : list) {
            Entity entity = (Entity) o;

            if (entity.canBeCollidedWith()) {
                float f2 = entity.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f2, f2, f2);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        pointedEntity = entity;
                        minDistance = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

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
}
