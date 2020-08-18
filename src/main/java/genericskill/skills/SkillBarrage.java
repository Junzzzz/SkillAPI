package genericskill.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class SkillBarrage extends SkillGeneric {
    @Override
    public int getManaCost(EntityPlayer player) {
        return 10;
    }

    @Override
    public float getChargeupTime(EntityPlayer player) {
        return 2;
    }

    @Override
    public float getCooldownTime(EntityPlayer player) {
        return 10;
    }

    @Override
    public boolean canPlayerUseSkill(EntityPlayer player) {
        return player.inventory.hasItem(Items.arrow);
    }

    @Override
    public boolean onSkillTrigger(EntityPlayer player) {
        // Check again just in case the player threw his arrows away while charging.
        if (!canPlayerUseSkill(player)) {
            return false;
        }
        World world = player.worldObj;
        float shotStrength = (player.experienceLevel + 1) / 1.7F;
        world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (player.getRNG().nextFloat() * 0.4F + 1.2F) + 0.5F);
        for (int i = 0; i < 5; i++) {
            if (player.inventory.consumeInventoryItem(Items.arrow) && !world.isRemote) {
                world.spawnEntityInWorld(new EntityArrow(world, player, shotStrength - (player.getRNG().nextFloat() * player.getRNG().nextFloat() * player.getRNG().nextFloat())));
            }
        }
        return true;
    }
}
