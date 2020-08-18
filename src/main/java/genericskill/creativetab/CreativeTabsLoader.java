package genericskill.creativetab;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;

/**
 * @author Jun
 * @date 2020/8/19.
 */
public class CreativeTabsLoader {
    public static CreativeTabs tabSkill;

    public CreativeTabsLoader(FMLPreInitializationEvent event) {
        tabSkill = new CreativeTabsSkill();
    }
}
