package genericskill.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import skillapi.item.SkillItemLoader;

public class SkillBindingSignet extends SkillGeneric {
    @Override
    public int getManaCost(EntityPlayer player) {
        return 0;
    }

    @Override
    public float getChargeupTime(EntityPlayer player) {
        return 10;
    }

    @Override
    public float getCooldownTime(EntityPlayer player) {
        return 90;
    }

    @Override
    public boolean canPlayerUseSkill(EntityPlayer player) {
        return player.inventory.hasItem(SkillItemLoader.heritageAmulet);
    }

    @Override
    public boolean onSkillTrigger(EntityPlayer player) {
        if (canPlayerUseSkill(player)) {
            if (!player.worldObj.isRemote) {
                if (!player.worldObj.provider.canRespawnHere()) {
                    player.travelToDimension(0);
                }
                ChunkCoordinates chunkcoordinates = player.worldObj.getSpawnPoint();
                int posX = chunkcoordinates.posX;
                int posY = chunkcoordinates.posY;
                int posZ = chunkcoordinates.posZ;
                while (!player.worldObj.isAirBlock(posX, posY, posZ)) {
                    posY++; //So you don't spawn in the floor.
                }
                player.setPositionAndUpdate(posX + 0.5F, posY + 0.1F, posZ + 0.5F);
            }
            player.inventory.consumeInventoryItem(SkillItemLoader.heritageAmulet);
            player.worldObj.playSoundAtEntity(player, "mob.ghast.fireball", 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }
}
