package skillapi.server;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import skillapi.api.SkillApi;
import skillapi.common.SkillProxy;

/**
 * @author Jun
 */
public class SkillServerProxy implements SkillProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        SkillApi.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        SkillApi.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}