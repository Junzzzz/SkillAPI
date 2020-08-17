package genericskill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import skillapi.SkillRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ItemSkillBook extends Item {
    private Collection<String> skills = new ArrayList<String>();

    /**
     * Generic item with already set unlocalized name and texture.
     * Can teach skills on player right click
     */
    public ItemSkillBook() {
        super();
        setUnlocalizedName("genSkillBook");
        setTextureName("genericskill:genskillbook");
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        int learnt = 0;
        for (String skillName : skills) {
            learnt += SkillRegistry.learnSkill(player, skillName) ? 1 : 0;
        }
        if (learnt <= 0) {
            return itemstack;
        }
        itemstack.stackSize--;
        return itemstack;
    }

    /**
     * Utility method to add skills to the book
     *
     * @param skillNames to add to the book, case sensitive
     * @return the book, for chaining convenience
     */
    public ItemSkillBook addSkills(String... skillNames) {
        Collections.addAll(skills, skillNames);
        return this;
    }
}