package com.github.junzzzz.skillapi.item;

import com.github.junzzzz.skillapi.client.tab.SkillCreativeTabs;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author Jun
 * @date 2020/8/19.
 */
public class SkillItemLoader {
    public static final CreativeTabs TAB = new SkillCreativeTabs();

    public static void register(Item item, String name) {
        item.setCreativeTab(TAB);
        GameRegistry.registerItem(item, name);
    }
}
