package skillapi.newpacket.base;

import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.common.SkillLog;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jun
 * @date 2020/8/28.
 */
public final class SkillPacketHandler {
    public final static String CHANNEL_NAME;
    public final static FMLEventChannel CHANNEL;
    private final static PacketReceiveEvent RECEIVE_EVENT;

    private static Map<String, SkillPacketStructure> packetMap = new HashMap<>(32);
    private static Map<String, String> classMap = new HashMap<>(32);

    static {
        CHANNEL_NAME = "SkillChannel";
        CHANNEL = NetworkRegistry.INSTANCE.newEventDrivenChannel(CHANNEL_NAME);
        RECEIVE_EVENT = new PacketReceiveEvent();
        CHANNEL.register(RECEIVE_EVENT);
    }

    public static String register(String name, SkillPacketStructure skillPacket) {
        name = renamePacket(name);
        packetMap.put(name, skillPacket);
        classMap.put(skillPacket.getName(), name);
        return name;
    }

    private static String renamePacket(String name) {
        String newName = name;
        for (int i = 1; isRegistered(newName); i++) {
            newName = name + "-" + i;
        }
        if (!newName.equals(name)) {
            SkillLog.warn("The packet has the same name [%s] and has been automatically renamed to [%s]", name, newName);
        }
        return newName;
    }

    public static boolean isRegistered(String name) {
        return packetMap.get(name) != null;
    }

    public static void distribute(String name, byte[] data, EntityPlayer player) {
        final SkillPacketStructure info = packetMap.get(name);
        if (info == null) {
            SkillLog.error("Data distribution failed: Can't find the corresponding data packet.");
            return;
        }
        final BaseSkillPacket packet = info.newInstance(data);
        if (packet == null) {
            return;
        }
        packet.run(player);
    }

    public static String getPacketName(Class<? extends BaseSkillPacket> packetClass) {
        return classMap.get(packetClass.getName());
    }
}
