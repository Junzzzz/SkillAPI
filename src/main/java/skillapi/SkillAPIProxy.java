package skillapi;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;

public class SkillAPIProxy {
    public void loadSkillKeyBindings() {
    }

    public void updateKeyBindingTypes(EntityPlayer player) {
    }

    public void register() {
        FMLCommonHandler.instance().bus().register(SkillTickHandler.INSTANCE);
    }

    public EntityPlayer getPlayer() {
        return null;
    }
}
