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
    public void setCooldown(long millis) {
        this.cooldown = millis * 1000000;
    }

    @Override
    public void setCooling() {
        this.initTime = true;
        this.lastTime = System.nanoTime();
    }
}
