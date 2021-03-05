package skillapi.packets;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.network.NetHandlerPlayServer;
import skillapi.Application;

import java.util.HashMap;
import java.util.Map;

public class SkillPacketHandler {
    public static final String[] CHANNELS = {"APISKILLINIT", "APISKILLGET", "APISKILLUPDATE", "APISKILLTRIGGER", "APISKILLACTIVATE", "APISKILLTICK", "APISKILLMANA"};
    public static Map<String, SkillPacket> packets = new HashMap<String, SkillPacket>();

    private int count = 0;

    static {
        packets.put(CHANNELS[0], new InitSkillPacket());
        packets.put(CHANNELS[1], new LearnSkillPacket());
        packets.put(CHANNELS[2], new UpdateSkillPacket());
        packets.put(CHANNELS[3], new TriggerSkillPacket());
        packets.put(CHANNELS[4], new ActiveSkillPacket());
        packets.put(CHANNELS[5], new TickDataSkillPacket());
        packets.put(CHANNELS[6], new ManaSpentPacket());
    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        System.out.println(Thread.currentThread().getName() + " " + event.packet.channel() + " " + (++count));
        SkillPacket skpacket = packets.get(event.packet.channel());
        if (skpacket != null) {
            skpacket.fromBytes(event.packet.payload());
            if (skpacket.run(((NetHandlerPlayServer) event.handler).playerEntity)) {
                FMLProxyPacket proxy = skpacket.getPacket(Side.CLIENT);
                proxy.setDispatcher(event.packet.getDispatcher());
                event.reply = proxy;
            }
        }
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        System.out.println(Thread.currentThread().getName() + " " + event.packet.channel() + " " + (++count));
        SkillPacket skpacket = packets.get(event.packet.channel());
        if (skpacket != null) {
            skpacket.fromBytes(event.packet.payload());
            skpacket.run(Application.oldProxy.getPlayer());
        }
    }
}
