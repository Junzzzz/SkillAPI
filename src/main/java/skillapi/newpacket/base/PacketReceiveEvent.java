package skillapi.newpacket.base;

import com.google.common.base.Charsets;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetHandlerPlayServer;

/**
 * @author Jun
 * @date 2020/8/28.
 */
public final class PacketReceiveEvent {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientPacketEvent(ClientCustomPacketEvent event) {
        System.out.println("process");
        process(event.packet.payload(), FMLClientHandler.instance().getClient().thePlayer);
    }

    @SubscribeEvent
    public void onServerPacketEvent(ServerCustomPacketEvent event) {
        process(event.packet.payload(), ((NetHandlerPlayServer) event.handler).playerEntity);
    }

    private void process(ByteBuf buffer, EntityPlayer player) {
        byte[] nameData = new byte[buffer.readByte()];
        buffer.readBytes(nameData);
        final String name = new String(nameData, Charsets.UTF_8);

        byte[] jsonData = new byte[buffer.readableBytes()];
        buffer.readBytes(jsonData);
        SkillPacketHandler.distribute(name, jsonData, player);
        buffer.clear();
    }
}
