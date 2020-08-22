package skillapi.server;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import skillapi.common.SkillProxy;

/**
 * @author Jun
 * @date 2020/8/20.
 */
public class SkillServerProxy implements SkillProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {
        System.out.println("Server init!");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }
}
