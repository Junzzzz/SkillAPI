package io.github.junzzzz.skillapi.server;

import io.github.junzzzz.skillapi.skill.Cooldown;

/**
 * @author Jun
 */
public class ServerCooldown implements Cooldown {
    private long cooldown;
    private long lastTime;

    public ServerCooldown(long cooldownMillis) {
        this.cooldown = cooldownMillis;
        this.lastTime = -1;
    }

    @Override
    public boolean isCooledDown() {
        return this.lastTime != -1 && System.currentTimeMillis() - this.lastTime >= cooldown;
    }

    @Override
    public double getCooledRate() {
        if (this.lastTime == -1 || cooldown == 0) {
            return 1.0D;
        }
        return Math.min(1.0D, (System.currentTimeMillis() - this.lastTime) * 1.0D / cooldown);
    }

    @Override
    public void setCooldown(long millis) {
        this.cooldown = millis;
    }

    @Override
    public void setCooling() {
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return System.nanoTime() - this.lastTime + "ms";
    }
}
