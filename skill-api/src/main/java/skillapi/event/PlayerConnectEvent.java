package skillapi.event;

import cpw.mods.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.network.NetHandlerPlayServer;
import skillapi.api.annotation.SkillEvent;
import skillapi.event.base.BaseSkillEvent;
import skillapi.newpacket.SkillConfigPacket;
import skillapi.skill.SkillLocalConfig;

/**
 * @author Jun
 * @date 2020/9/10.
 */
@SkillEvent(Side.SERVER)
public class PlayerConnectEvent extends BaseSkillEvent<ServerConnectionFromClientEvent> {
    @Override
    protected void onServer(ServerConnectionFromClientEvent event) {
        final FMLProxyPacket fmlProxyPacket =
                new SkillConfigPacket(SkillLocalConfig.SERVER_CONFIG).processPacket(Side.CLIENT);
        System.out.println(fmlProxyPacket.channel());
        ((NetHandlerPlayServer) event.handler).sendPacket(fmlProxyPacket);
    }

    @Override
    protected void onClient(ServerConnectionFromClientEvent event) {
        // Only happens on the server
    }
}
