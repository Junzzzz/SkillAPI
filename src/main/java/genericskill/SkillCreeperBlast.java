package genericskill;

import net.minecraft.entity.player.EntityPlayer;

public class SkillCreeperBlast extends SkillGeneric {
    @Override
    public String getDescription() {
        return "Channels your inner creeper\noutwards into an explosive\nforce.";
    }

    @Override
    public int getManaCost(EntityPlayer player) {
        return 20;
    }

    @Override
    public float getChargeupTime(EntityPlayer player) {
        return 3;
    }

    @Override
    public float getCooldownTime(EntityPlayer player) {
        return 30;
    }

    @Override
    public boolean onSkillTrigger(EntityPlayer player) {
        player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, 3F, true);
        return true;
    }
}
