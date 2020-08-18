package genericskill.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import skillapi.PlayerSkills;

public class ItemManaPotion extends Item {
    private final int restoreAmount;

    /* Items can use mana too! */
    public ItemManaPotion(int restoreAmount) {
        super();
        setUnlocalizedName("manaPotion");
        setTextureName("genericskill:manapotion");
        setMaxStackSize(16);
        this.restoreAmount = restoreAmount;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        PlayerSkills sk = PlayerSkills.get(player);
        if (sk.getMana() < 20) {
            sk.restoreMana(restoreAmount);
            itemstack.stackSize--; //Keep this if you want the item to be consumed.
        }
        return itemstack;
    }
}