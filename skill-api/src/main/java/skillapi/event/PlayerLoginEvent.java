package skillapi.event;

import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import skillapi.api.annotation.SkillEvent;
import skillapi.event.base.BaseSkillEvent;

/**
 * @author Jun
 */
@SkillEvent(Side.SERVER)
public class PlayerLoginEvent extends BaseSkillEvent<PlayerLoggedInEvent> {
    @Override
    public void onServer(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
//            Packet.sendToClient(new TestPacket("你登录了！"), (EntityPlayerMP) event.player);
        }

    }

    @Override
    public void onClient(PlayerLoggedInEvent event) {
        // Only happens on the server
    }
}
