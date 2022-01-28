package skillapi.packet.base;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.serializer.CallbackResultSerializer;

import java.util.UUID;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket(serializer = CallbackResultSerializer.class)
public class CallbackResultPacket<T> extends AbstractPacket {
    private UUID uuid;
    private T data;

    @Override
    protected void run(EntityPlayer player, Side from) {
        Packet.callback(this.uuid, this.data);
    }
}
