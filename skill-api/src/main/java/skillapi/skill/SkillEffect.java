package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
public interface SkillEffect {
    void unleash(EntityPlayer player, EntityLivingBase entity);

    /**
     * Fully qualified name
     */
    String getUnlocalizedName();
}
