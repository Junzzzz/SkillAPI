package com.github.junzzzz.skillapi.client;

import com.github.junzzzz.skillapi.server.ServerCooldown;
import com.github.junzzzz.skillapi.server.SkillServerProxy;
import com.github.junzzzz.skillapi.skill.Cooldown;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

/**
 * @author Jun
 */
public class SkillClientProxy extends SkillServerProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        SkillClient.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public Cooldown getCooldown(long cooldownMills) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            return new ClientCooldown(cooldownMills);
        } else {
            return new ServerCooldown(cooldownMills);
        }
    }
}
