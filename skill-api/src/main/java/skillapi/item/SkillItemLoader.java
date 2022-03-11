package skillapi.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import skillapi.client.tab.SkillCreativeTabs;

/**
 * @author Jun
 * @date 2020/8/19.
 */
public class SkillItemLoader {
    public static final CreativeTabs TAB = new SkillCreativeTabs();

//    public static Item genSkillBook = new ItemSkillBook().addSkills(skills);
//    public static Item heritageAmulet = new ItemHeritageAmulet();
//    public static Item manaPotion = new ItemManaPotion(5);

    public static void preInit() {
        // TODO CLEAR
//        register(genSkillBook, "generic_skill_book");
//        register(heritageAmulet, "heritage_amulet");
//        register(manaPotion, "mana_potion");
    }

    public static void register(Item item, String name) {
        item.setCreativeTab(TAB);
        GameRegistry.registerItem(item, name);
    }
}
