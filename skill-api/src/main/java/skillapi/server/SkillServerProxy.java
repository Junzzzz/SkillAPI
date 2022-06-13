package skillapi.server;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import skillapi.Application;
import skillapi.api.SkillApi;
import skillapi.common.SkillProxy;
import skillapi.potion.SkillPotions;
import skillapi.skill.Cooldown;

/**
 * @author Jun
 */
public class SkillServerProxy implements SkillProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        // TODO replace package
        SkillApi.preInit(event, Application.MOD_ID);
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