package genericskill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class SkillUnrelentingForce extends SkillGeneric {
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
        return 15;
    }

    @Override
    public boolean onSkillTrigger(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            player.worldObj.spawnEntityInWorld(new EntityShockWave(player.worldObj, player));
        }
        player.addChatComponentMessage(new ChatComponentText(player.getCommandSenderName() + ": Fus Ro Dah!"));
        player.worldObj.playSoundAtEntity(player, "random.explode", 0.2F, 1.0F);
        MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(player);
        if (movingobjectposition == null) {
            return true;
        }
        if (movingobjectposition.entityHit != null) {
            movingobjectposition.entityHit.addVelocity(-Math.sin(Math.toRadians(player.rotationYaw)) * Math.cos(Math.toRadians(player.rotationPitch)) * 3,
                    (0.5 - Math.sin(Math.toRadians(player.rotationPitch))) * 3, Math.cos(Math.toRadians(player.rotationYaw)) * Math.cos(Math.toRadians(player.rotationPitch)) * 3);
        } else if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            for (int x = movingobjectposition.blockX - 1; x < movingobjectposition.blockX + 2; x++) {
                for (int y = movingobjectposition.blockY - 1; y < movingobjectposition.blockY + 2; y++) {
                    for (int z = movingobjectposition.blockZ - 1; z < movingobjectposition.blockZ + 2; z++) {
                        player.worldObj.setBlockToAir(x, y, z);
                    }
                }
            }
        }
        return true;
    }

    protected MovingObjectPosition getMovingObjectPositionFromPlayer(EntityPlayer player) {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * f + (player.worldObj.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight());
        // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 10.0D;
        Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
        return player.worldObj.rayTraceBlocks(vec3, vec31, true);
    }
}
