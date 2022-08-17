package io.github.junzzzz.skillapi.packet.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cpw.mods.fml.relauncher.Side;
import io.github.junzzzz.skillapi.packet.serializer.PacketSerializer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 */
public abstract class AbstractPacket {
    @JsonIgnore
    public final PacketSerializer<? extends AbstractPacket> getSerializer() {
        return Packet.getPacketSerializer(this.getClass());
    }

    protected abstract void run(EntityPlayer player, Side from);
}
