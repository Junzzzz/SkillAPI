package com.github.junzzzz.skillapi.client.tab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * @author Jun
 */
public class SkillCreativeTabs extends CreativeTabs {
    public SkillCreativeTabs() {
        super("SkillAPI");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        // TODO replace
        return Items.book;
    }
}
