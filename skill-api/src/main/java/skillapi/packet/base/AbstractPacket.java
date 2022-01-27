package skillapi.packet.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.packet.serializer.PacketSerializer;

/**
 * @author Jun
 */
public abstract class AbstractPacket {
    @JsonIgnore
    public final PacketSerializer<? extends AbstractPacket>  getSerializer() {
        return Packet.getSerializer(this.getClass());
    }

    protected abstract void run(EntityPlayer player, Side from);
}
