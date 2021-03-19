package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 * @date 2020/9/29.
 */
public abstract class BaseSkillToLivingEntityEffect extends BaseSkillEffect {
    @Override
    protected final void effect(EntityPlayer player) {
        // ERROR
        throw new RuntimeException("This method is forbidden to call!");
    }

    protected abstract void effect(EntityPlayer player, EntityLivingBase entity);
}
