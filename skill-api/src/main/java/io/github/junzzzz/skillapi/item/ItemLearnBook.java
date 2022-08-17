package io.github.junzzzz.skillapi.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.junzzzz.skillapi.Application;
import io.github.junzzzz.skillapi.api.annotation.SkillItem;
import io.github.junzzzz.skillapi.common.Message;
import io.github.junzzzz.skillapi.packet.PlayerLearnSkillPacket;
import io.github.junzzzz.skillapi.packet.base.Packet;
import io.github.junzzzz.skillapi.skill.AbstractSkill;
import io.github.junzzzz.skillapi.skill.PlayerSkills;
import io.github.junzzzz.skillapi.skill.Skills;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author Jun
 */
@SkillItem("skill_learn_book")
public class ItemLearnBook extends Item {
    private static final String TAG_NAME = "skill";

    public ItemLearnBook() {
        super();

        setUnlocalizedName("skillLearnBook");
        setTextureName(Application.MOD_ID + ":skill_learn_book");
        setMaxStackSize(1);

        // Set has Metadata
        setHasSubtypes(true);

        // Avoid some bugs
        setMaxDamage(0);
        setNoRepair();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (!hasTag(itemStack)) {
            if (world.isRemote) {
                // Client
                Message.sendTranslation(player, "skill.constant.invalidLeanBook");
            }
            itemStack.stackSize = 0;
            return itemStack;
        }

        AbstractSkill skill = getSkill(itemStack);
        if (skill == null) {
            if (world.isRemote) {
                // Client
                Message.sendTranslation(player, "skill.constant.invalidLeanBook");
            }
            itemStack.stackSize = 0;
            return itemStack;
        }

        if (!world.isRemote) {
            // Server
            PlayerSkills playerSkills = PlayerSkills.get(player);
            if (playerSkills.learnSkill(skill)) {
                Packet.send(new PlayerLearnSkillPacket(skill.getUnlocalizedName()), player);
                itemStack.stackSize = 0;
            } else {
                Message.sendTranslation(player, "skill.constant.knownSkill");
            }
        }

        return itemStack;
    }

    private boolean hasTag(ItemStack itemStack) {
        return itemStack.stackTagCompound != null && itemStack.stackTagCompound.hasKey(TAG_NAME, 10);
    }

    private AbstractSkill getSkill(ItemStack itemStack) {
        NBTTagCompound tag = itemStack.stackTagCompound.getCompoundTag(TAG_NAME);
        String name = tag.getString("name");
        if (name.isEmpty()) {
            return null;
        }
        return Skills.get(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List information, boolean isAdvanced) {
        if (!hasTag(itemStack)) {
            information.add(EnumChatFormatting.RED + I18n.format("skill.constant.invalidLeanBook"));
            return;
        }
        AbstractSkill skill = getSkill(itemStack);
        if (skill == null) {
            information.add(EnumChatFormatting.RED + I18n.format("skill.constant.invalidLeanBook"));
        } else {
            information.add(EnumChatFormatting.BLUE + I18n.format("skill.constant.skillBook") + Skills.getLocalizedName(skill));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (AbstractSkill skill : Skills.getAll()) {
            ItemStack itemStack = new ItemStack(item);
            NBTTagCompound skillTag = new NBTTagCompound();
            itemStack.setTagInfo(TAG_NAME, skillTag);
            skillTag.setString("name", skill.getUnlocalizedName());
            list.add(itemStack);
        }
    }
}
