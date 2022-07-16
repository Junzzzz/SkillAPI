package skillapi.skill;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
public interface SkillEffect {
    @SideOnly(Side.CLIENT)
    boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo);

    boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo);

    /**
     * Will be executed on the server side
     */
    void unleash(EntityPlayer player, SkillExtraInfo extraInfo);

    void afterUnleash(EntityPlayer player, SkillExtraInfo extraInfo);

    /**
     * Used to perform client side effects
     */
    @SideOnly(Side.CLIENT)
    void clientUnleash(EntityPlayer player, SkillExtraInfo extraInfo);

    /**
     * Fully qualified name
     */
    String getUnlocalizedName();
}
