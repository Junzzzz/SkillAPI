package io.github.junzzzz.genericeffects;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.github.junzzzz.skillapi.api.SkillApi;

@Mod(modid = "generic-effects", name = "Generic Skill Effects Pack", useMetadata = true, dependencies = "required-after:skillapi")
public final class GenericEffects {

    @EventHandler
    public void init(FMLInitializationEvent event) {
        SkillApi.init(event);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SkillApi.preInit(event, "io.github.junzzzz.genericeffects");
    }
}