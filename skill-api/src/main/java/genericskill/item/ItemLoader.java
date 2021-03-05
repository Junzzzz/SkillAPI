package genericskill.item;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import genericskill.creativetab.CreativeTabsLoader;
import net.minecraft.item.Item;

import static genericskill.GenericSkills.skills;

/**
 * @author Jun
 * @date 2020/8/19.
 */
public class ItemLoader {
    public static Item genSkillBook = new ItemSkillBook().addSkills(skills);
    public static Item heritageAmulet = new ItemHeritageAmulet();
    public static Item manaPotion = new ItemManaPotion(5);

    public static Item learnBook = new ItemLearnBook("none");

    public ItemLoader(FMLPreInitializationEvent event) {
        register(learnBook, "generic_learn_book");
        register(genSkillBook, "generic_skill_book");
        register(heritageAmulet, "heritage_amulet");
        register(manaPotion, "mana_potion");


    }

    private static void register(Item item, String name) {
        item.setCreativeTab(CreativeTabsLoader.tabSkill);
        GameRegistry.registerItem(item, name);
    }
}
