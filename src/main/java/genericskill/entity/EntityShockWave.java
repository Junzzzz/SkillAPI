package genericskill.entity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityShockWave extends Entity {
    private double radius = 0.2;
    private Entity owner;

    public EntityShockWave(World world) {
        super(world);
        setSize(0, 0);
    }

    public EntityShockWave(World world, Entity source) {
        this(world);
        this.owner = source;
        setLocationAndAngles(source.posX, source.posY, source.posZ, source.rotationYaw, source.rotationPitch);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.worldObj.isRemote) {
            int angle = 0;
            while (angle < 360) {
                spawnParticles(angle);
                angle += 10;
            }
        }
        moveEntity(-Math.sin(Math.toRadians(rotationYaw)) * Math.cos(Math.toRadians(rotationPitch)), -Math.sin(Math.toRadians(rotationPitch)),
                Math.cos(Math.toRadians(rotationYaw)) * Math.cos(Math.toRadians(rotationPitch)));
        if (!this.worldObj.isRemote) {
            this.collideWithNearbyEntities();
        }
        radius += 0.15;
        if (radius > 5) {
            setDead();
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles(int angle) {
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityBrightReddustFX(worldObj, posX + radius * Math.cos(Math.toRadians(rotationYaw))
                * Math.cos(Math.toRadians(rotationPitch)) * Math.cos(Math.toRadians(angle)), posY + radius * Math.sin(Math.toRadians(angle)), posZ + radius * Math.sin(Math.toRadians(rotationYaw))
                * Math.cos(Math.toRadians(rotationPitch)) * Math.cos(Math.toRadians(angle)), 1, 0, 191, 255));
    }

    protected void collideWithNearbyEntities() {
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(owner, this.boundingBox.expand(1.0D, 1.0D, 1.0D));
        if (list != null && !list.isEmpty()) {
            for (Entity entity : list) {
                if (entity.canBePushed()) {
                    double d0 = entity.posX - this.posX;
                    double d1 = entity.posZ - this.posZ;
                    double d2 = MathHelper.abs_max(d0, d1);
                    if (d2 >= 0.01D) {
                        d2 = MathHelper.sqrt_double(d2);
                        d0 /= d2;
                        d1 /= d2;
                        double d3 = 1.0D / d2;
                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }
                        d0 *= d3 * (1.0F - entity.entityCollisionReduction);
                        d1 *= d3 * (1.0F - entity.entityCollisionReduction);
                        entity.addVelocity(d0, 0.0D, d1);
                    }
                }
            }
        }
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
    }
}