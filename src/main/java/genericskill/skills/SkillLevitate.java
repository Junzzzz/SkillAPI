package genericskill.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.world.WorldServer;

public class SkillLevitate extends SkillGeneric {
    @Override
    public int getManaCost(EntityPlayer player) {
        return 1;
    }

    @Override
    public float getChargeupTime(EntityPlayer player) {
        return 0;
    }

    @Override
    public float getCooldownTime(EntityPlayer player) {
        return 0;
    }

    @Override
    public boolean onSkillTrigger(EntityPlayer player) {
        return true;
    }

    @Override
    public void onTickWhileActive(EntityPlayer player) {
        player.addVelocity(0.0D, 0.1D, 0.0D);
        if (player.fallDistance > 0) {
            player.fallDistance = 0;
        }
        if (player.worldObj instanceof WorldServer) {
            ((WorldServer) player.worldObj).getEntityTracker().func_151248_b(player, new S12PacketEntityVelocity(player));
        }
    }
}
