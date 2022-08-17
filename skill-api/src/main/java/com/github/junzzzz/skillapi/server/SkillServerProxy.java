package com.github.junzzzz.skillapi.server;

import com.github.junzzzz.skillapi.Application;
import com.github.junzzzz.skillapi.api.SkillApi;
import com.github.junzzzz.skillapi.common.SkillProxy;
import com.github.junzzzz.skillapi.potion.SkillPotions;
import com.github.junzzzz.skillapi.skill.Cooldown;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Jun
 */
public class SkillServerProxy implements SkillProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        SkillApi.preInit(event, Application.PACKAGE_PREFIX + Application.MOD_ID);
        SkillPotions.insurePotionSize(64);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        SkillApi.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public Cooldown getCooldown(long cooldownMills) {
        return new ServerCooldown(cooldownMills);
    }
}