package genericskill.creativetab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import genericskill.GenericSkills;
import genericskill.item.ItemLoader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author Jun
 * @date 2020/8/19.
 */
public class CreativeTabsSkill extends CreativeTabs {
    public CreativeTabsSkill() {
        super("GenericSkillPack");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return ItemLoader.heritageAmulet;
    }
}
