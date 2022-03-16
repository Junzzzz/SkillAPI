package skillapi.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import skillapi.server.ServerCooldown;
import skillapi.server.SkillServerProxy;
import skillapi.skill.Cooldown;

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
