package skillapi;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class SkillAPIProxy {
    public void loadSkillKeyBindings() {
    }

    public void updateKeyBindingTypes(EntityPlayer player) {
    }

    public void register() {
        FMLCommonHandler.instance().bus().register(SkillTickHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(PlayerEventHandler.INSTANCE);
        FMLCommonHandler.instance().bus().register(PlayerEventHandler.INSTANCE);
    }

    public EntityPlayer getPlayer() {
        return null;
    }
}
