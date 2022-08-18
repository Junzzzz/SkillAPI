package io.github.junzzzz.skillapi.item;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.junzzzz.skillapi.client.tab.SkillCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author Jun
 */
public class SkillItemLoader {
    public static final CreativeTabs TAB = new SkillCreativeTabs();

    public static void register(Item item, String name) {
        item.setCreativeTab(TAB);
        GameRegistry.registerItem(item, name);
    }
}
