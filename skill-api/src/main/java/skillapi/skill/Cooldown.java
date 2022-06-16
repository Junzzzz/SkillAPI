package skillapi.skill;

import skillapi.Application;

/**
 * @author Jun
 */
public interface Cooldown {
    boolean isCooledDown();

    double getCooledRate();

    void setCooldown(long millis);

    void setCooling();

    static Cooldown get(long cooldownMills) {
        return Application.proxy.getCooldown(cooldownMills);
    }
}
