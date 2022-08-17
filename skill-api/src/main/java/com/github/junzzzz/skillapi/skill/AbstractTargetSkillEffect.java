package com.github.junzzzz.skillapi.skill;

import com.github.junzzzz.skillapi.api.annotation.SkillEffect;
import com.github.junzzzz.skillapi.api.annotation.SkillParam;
import com.github.junzzzz.skillapi.utils.ClientUtils;
import com.github.junzzzz.skillapi.utils.MathUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import javax.vecmath.Vector3d;
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
        if (this.width == -1 && this.length == -1) {
            return true;
        }
        if (this.width <= 0 || this.length <= 0) {
            return false;
        }
        if (extraInfo.get(KEY_TARGET) != null) {
            return true;
        }
        double height = player.boundingBox.maxY - player.boundingBox.minY;
        List<EntityLivingBase> targets = ClientUtils.getPointedDirectionEntitiesByBox(this.width, height, this.length);
        if (targets.size() > 0) {
            int[] result = new int[targets.size()];
            for (int i = 0; i < targets.size(); i++) {
                result[i] = targets.get(i).getEntityId();
            }
            extraInfo.put(KEY_TARGET, result);
            targetsCache = targets;
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private void loadExtraInfo(EntityPlayer player, SkillExtraInfo extraInfo) {
        SkillExtraInfo.ExtraObject extraObject = extraInfo.getExtraObject(KEY_TARGET);
        if (extraObject.clz == int[].class) {
            int[] ids = (int[]) extraObject.obj;
            List<EntityLivingBase> targets = new ArrayList<>(ids.length);
            for (int id : ids) {
                targets.add((EntityLivingBase) player.getEntityWorld().getEntityByID(id));
            }
            extraInfo.replace(KEY_TARGET, targets);
            targetsCache = targets;
        } else if (extraObject.obj instanceof List) {
            targetsCache = (List<EntityLivingBase>) extraObject.obj;
        }
    }

    @Override
    public final boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        if (this.width == -1 && this.length == -1) {
            return true;
        }
        loadExtraInfo(player, extraInfo);
        return canUnleash(player, targetsCache, extraInfo);
    }

    @Override
    public final void unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        if (this.width == -1 && this.length == -1) {
            unleash(player, null, extraInfo);
            return;
        }
        if (targetsCache != null) {
            for (EntityLivingBase target : targetsCache) {
                unleash(player, target, extraInfo);
            }
        }
    }


    @Override
    public void clientUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        super.clientUnleash(player, extraInfo);
        if (targetsCache != null) {
            extraInfo.replace(KEY_TARGET, targetsCache);
        } else {
            loadExtraInfo(player, extraInfo);
        }

        if (targetsCache != null) {
            for (EntityLivingBase target : targetsCache) {
                clientUnleash(player, target, extraInfo);
            }
            targetsCache.clear();
            targetsCache = null;
        }
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

    public abstract void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo);

    public void clientUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        Vector3d position = MathUtils.tranform(player.getPosition(1.0F));
        Vector3d look = MathUtils.tranform(player.getLookVec());
        ClientUtils.spawnParticle(position, look);
    }

    public void afterUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        // Default: none
    }
}
