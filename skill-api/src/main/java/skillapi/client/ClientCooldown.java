package skillapi.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import skillapi.skill.Cooldown;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
public class ClientCooldown implements Cooldown {
    private long cooldown;
    private long lastTime;
    private boolean initTime;

    public ClientCooldown(long cooldownMillis) {
        this.cooldown = cooldownMillis * 1000000;
        this.initTime = false;
    }

    @Override
    public boolean isCooledDown() {
        return this.initTime && (System.nanoTime() - this.lastTime >= cooldown);
    }

    @Override
    public double getCooledRate() {
        if (!this.initTime || cooldown == 0) {
            return 1.0D;
        }
        return Math.min(1.0D, (System.nanoTime() - this.lastTime) * 1.0D / cooldown);
    }

    @Override
    public void setCooldown(long millis) {
        this.cooldown = millis * 1000000;
    }

    @Override
    public void setCooling() {
        this.initTime = true;
        this.lastTime = System.nanoTime();
    }

    @Override
    public String toString() {
        return (System.nanoTime() - this.lastTime) / 1000000 + "ms";
    }
}
