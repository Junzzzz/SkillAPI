package genericskill.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import skillapi.SkillRegistry;

public class ItemHeritageAmulet extends Item {
    public ItemHeritageAmulet() {
        super();
        setUnlocalizedName("heritageAmulet");
        setTextureName("genericskills:heritage_amulet");
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        SkillRegistry.triggerSkill(player, "Binding Signet");
        return itemstack;
    }
}