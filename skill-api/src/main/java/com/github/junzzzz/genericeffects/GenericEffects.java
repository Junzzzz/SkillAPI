package com.github.junzzzz.genericeffects;

import com.github.junzzzz.skillapi.api.SkillApi;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "generic-effects", name = "Generic Skill Effects Pack", useMetadata = true, dependencies = "required-after:skillapi")
public final class GenericEffects {

    @EventHandler
    public void init(FMLInitializationEvent event) {
        SkillApi.init(event);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SkillApi.preInit(event, "com.github.junzzzz.genericeffects");
    }
}