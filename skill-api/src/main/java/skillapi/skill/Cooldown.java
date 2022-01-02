package skillapi.skill;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.client.ClientCooldown;
import skillapi.server.ServerCooldown;

/**
 * @author Jun
 */
public interface Cooldown {
    boolean isCooledDown();

    void setCooldown(long millis);

    void setCooling();

    static Cooldown get(EntityPlayer player, long cooldownMills) {
        if (player instanceof AbstractClientPlayer) {
            return new ClientCooldown(cooldownMills);
        } else {
            return new ServerCooldown(cooldownMills);
        }
    }
}
