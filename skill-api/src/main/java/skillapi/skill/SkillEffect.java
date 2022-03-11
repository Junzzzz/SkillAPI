package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
public interface SkillEffect {
    boolean canUnleash(EntityPlayer player, EntityLivingBase entity);

    boolean unleash(EntityPlayer player, EntityLivingBase entity);

    void afterUnleash(EntityPlayer player, EntityLivingBase entity);

    /**
     * Fully qualified name
     */
    String getUnlocalizedName();
}
