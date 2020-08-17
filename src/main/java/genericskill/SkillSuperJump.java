package genericskill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

public class SkillSuperJump extends SkillGeneric {
    @Override
    public int getManaCost(EntityPlayer player) {
        return 4;
    }

    @Override
    public float getChargeupTime(EntityPlayer player) {
        return 0.5F;
    }

    @Override
    public float getCooldownTime(EntityPlayer player) {
        return 4;
    }

    @Override
    public boolean onSkillTrigger(EntityPlayer player) {
        if (player.handleWaterMovement()) {
            player.motionY += 0.42D * 2;
        } else if (player.handleLavaMovement()) {
            player.motionY = 0.42D * 2;
        } else if (player.onGround) {
            player.motionY = 0.42D * 2;
            if (player.isPotionActive(Potion.jump)) {
                player.motionY += (player.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.2F;
            }
            if (player.isSprinting()) {
                float f = player.rotationYaw * 0.01745329F;
                player.motionX -= MathHelper.sin(f) * 0.2F;
                player.motionZ += MathHelper.cos(f) * 0.2F;
            }
            player.isAirBorne = true;
        }
        if (player.worldObj instanceof WorldServer) {
            ((WorldServer) player.worldObj).getEntityTracker().func_151248_b(player, new S12PacketEntityVelocity(player));
        }
        return true;
    }
}
