package io.github.junzzzz.skillapi.server;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.github.junzzzz.skillapi.Application;
import io.github.junzzzz.skillapi.api.SkillApi;
import io.github.junzzzz.skillapi.common.SkillProxy;
import io.github.junzzzz.skillapi.potion.SkillPotions;
import io.github.junzzzz.skillapi.skill.Cooldown;

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