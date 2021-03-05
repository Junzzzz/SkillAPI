package skillapi.skill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

/**
 * @author Jun
 * @date 2020/9/29.
 */
public abstract class BaseSkillToLivingEntityEffect extends BaseSkillEffect {
    public static String ENTITY_KEY = "TargetEntity";

    @Override
    protected final void effect(EntityPlayer player, Map<String, Object> params) {
        Integer id = (Integer) params.get(ENTITY_KEY);
        final Entity entity = player.worldObj.getEntityByID(id);
        if (entity instanceof EntityLivingBase) {
            effect(player, (EntityLivingBase) entity, params);
        } else {
            effect(player, null, params);
        }
    }

    protected abstract void effect(EntityPlayer player, EntityLivingBase entity, Map<String, Object> params);
}
