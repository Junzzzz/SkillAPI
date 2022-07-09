package skillapi.skill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.utils.ClientUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 */
@SkillEffect
public abstract class AbstractTargetSkillEffect extends AbstractSkillEffect {
    private static final String KEY_TARGET = "pointedEntity";
    private static final double DEFAULT_DISTANCE = 5.0D;

//    @SkillParam(universal = true)
//    protected double distance;

    @SkillParam(universal = true)
    protected double width;

    @SkillParam(universal = true)
    protected double length;

    /**
     * For single thread
     */
    private List<EntityLivingBase> targetsCache;

    @SideOnly(Side.CLIENT)
    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
//        if (this.distance == -1) {
//            return true;
//        }

//        EntityLivingBase target = ClientUtils.getPointedLivingEntity(this.distance > 0 ? this.distance : DEFAULT_DISTANCE);
//        if (target != null) {
//            extraInfo.put(KEY_TARGET, target.getEntityId());
//            return true;
//        }
        if (this.width == -1 && this.length == -1) {
            return true;
        }
        if (this.width <= 0 || this.length <= 0) {
            return false;
        }
        double height = player.boundingBox.maxY - player.boundingBox.minY;
        List<EntityLivingBase> targets = ClientUtils.getPointedDirectionEntitiesByBox(this.width, height, this.length);
        if (targets.size() > 0) {
            int[] result = new int[targets.size()];
            for (int i = 0; i < targets.size(); i++) {
                result[i] = targets.get(i).getEntityId();
            }
            extraInfo.put(KEY_TARGET, result);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        if (this.width == -1 && this.length == -1) {
            return true;
        }

        SkillExtraInfo.ExtraObject extraObject = extraInfo.getExtraObject(KEY_TARGET);
        if (extraObject.clz == int[].class) {
            int[] ids = (int[]) extraObject.obj;
            List<EntityLivingBase> targets = new ArrayList<>(ids.length);
            for (int id : ids) {
                targets.add((EntityLivingBase) player.getEntityWorld().getEntityByID(id));
            }
            extraInfo.replace(KEY_TARGET, targets);
            return canUnleash(player, targetsCache = targets, extraInfo);
        } else if (extraObject.obj instanceof List) {
            return canUnleash(player, targetsCache = (List<EntityLivingBase>) extraObject.obj, extraInfo);
        }

        return false;
    }

    @Override
    public final boolean unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        if (this.width == -1 && this.length == -1) {
            return unleash(player, null, extraInfo);
        }

        for (EntityLivingBase target : targetsCache) {
            unleash(player, target, extraInfo);
        }
        return true;
    }

    @Override
    public final void afterUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        if (this.width == -1 && this.length == -1) {
            afterUnleash(player, null, extraInfo);
            return;
        }

        for (EntityLivingBase target : targetsCache) {
            afterUnleash(player, target, extraInfo);
        }

        targetsCache.clear();
        targetsCache = null;
    }

    public boolean canUnleash(EntityPlayer player, List<EntityLivingBase> targets, SkillExtraInfo extraInfo) {
        List<EntityLivingBase> newTargets = new ArrayList<>(targets.size());
        for (EntityLivingBase target : targets) {
            if (canUnleash(player, target, extraInfo)) {
                newTargets.add(target);
            }
        }
        targetsCache = newTargets;
        return targetsCache.size() > 0;
    }

    public boolean canUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        return player != null;
    }

    public abstract boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo);

    public void afterUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        // Default: none
    }
}
