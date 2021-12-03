package skillapi.packet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
public abstract class AbstractPacket {
    @JsonIgnore
    public final PacketSerializer getSerializer() {
        return PacketHandler.getSerializer(this.getClass());
    }

    abstract void run(EntityPlayer player, Side from);
}
