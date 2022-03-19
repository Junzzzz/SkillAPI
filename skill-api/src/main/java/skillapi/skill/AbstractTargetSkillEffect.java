package skillapi.skill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillParam;
import skillapi.utils.ClientUtils;

/**
 * @author Jun
 */
public abstract class AbstractTargetSkillEffect extends AbstractSkillEffect {
    private static final String KEY_TARGET = "pointedEntity";
    private static final double DEFAULT_DISTANCE = 5.0D;

    @SkillParam
    private double distance;

    /**
     * For single thread
     */
    private EntityLivingBase targetCache;

    @SideOnly(Side.CLIENT)
    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        EntityLivingBase target = ClientUtils.getPointedLivingEntity(this.distance > 0 ? this.distance : DEFAULT_DISTANCE);
        if (target != null) {
            extraInfo.put(KEY_TARGET, target.getEntityId());
            return true;
        }
        return false;
    }

    @Override
    public final boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        SkillExtraInfo.ExtraObject idObject = extraInfo.getExtraObject(KEY_TARGET);
        if (idObject.clz == int.class) {
            EntityLivingBase entity = (EntityLivingBase) player.getEntityWorld().getEntityByID((int) idObject.obj);
            extraInfo.replace(KEY_TARGET, entity);
            targetCache = entity;
            return canUnleash(player, entity, extraInfo);
        }
        return canUnleash(player, targetCache = extraInfo.get(KEY_TARGET), extraInfo);
    }

    @Override
    public final boolean unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        return unleash(player, targetCache, extraInfo);
    }

    @Override
    public final void afterUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        afterUnleash(player, targetCache, extraInfo);
        targetCache = null;
    }

    public boolean canUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        return player != null;
    }

    public abstract boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo);

    public void afterUnleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        // Default: none
    }
}
