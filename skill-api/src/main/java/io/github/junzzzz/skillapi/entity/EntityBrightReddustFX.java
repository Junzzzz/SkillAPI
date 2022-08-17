package io.github.junzzzz.skillapi.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityBrightReddustFX extends EntityReddustFX {
    public EntityBrightReddustFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, 0, 0, 0);
        this.particleGravity = 0;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float f) {
        return 0;
    }

    @Override
    public float getBrightness(float f) {
        return 0;
    }
}
