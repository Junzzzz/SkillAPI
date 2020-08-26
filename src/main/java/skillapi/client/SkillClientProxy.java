package skillapi.client;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import skillapi.Application;
import skillapi.server.SkillServerProxy;

/**
 * @author Jun
 * @date 2020/8/20.
 */
public class SkillClientProxy extends SkillServerProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (Application.isLogicalServer) {
            super.preInit(event);
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        if (Application.isLogicalServer) {
            super.init(event);
        }
        System.out.println("Client init!");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if (Application.isLogicalServer) {
            super.postInit(event);
        }
    }
}
