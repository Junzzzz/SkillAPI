package genericskill;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import skillapi.api.SkillApi;

@Mod(modid = "genericskills", name = "Generic Skills Pack", useMetadata = true, dependencies = "required-after:skillapi")
public final class GenericSkills {

    @EventHandler
    public void init(FMLInitializationEvent event) {
        SkillApi.init(event);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SkillApi.preInit(event, "genericskill");
    }
}