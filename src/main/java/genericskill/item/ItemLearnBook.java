package genericskill.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import skillapi.SkillRegistry;

import java.util.List;

/**
 * @author Jun
 * @date 2020/8/18.
 */
public class ItemLearnBook extends Item {
    private String skillName;

    public ItemLearnBook(String skillName) {
        super();

        this.skillName = skillName;
        setUnlocalizedName("genLearnBook");
        setTextureName("genericskills:golden_egg");
        setMaxStackSize(1);

        // Set has Metadata
        setHasSubtypes(true);

        // Avoid some bugs
        setMaxDamage(0);
        setNoRepair();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (SkillRegistry.learnSkill(player, skillName)) {
            itemStack.stackSize--;
        }


        return itemStack;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
//        super.getSubItems(item, tab, list);
        for (int i = 0; i < 4; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
