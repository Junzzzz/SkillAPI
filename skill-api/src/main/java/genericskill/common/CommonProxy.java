package genericskill.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import genericskill.creativetab.CreativeTabsLoader;
import genericskill.item.ItemLoader;

/**
 * @author Jun
 * @date 2020/8/19.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        new CreativeTabsLoader(event);
        new ItemLoader(event);
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
