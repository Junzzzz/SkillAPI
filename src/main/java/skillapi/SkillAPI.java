package skillapi;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import skillapi.packets.SkillPacketHandler;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = "skillapi", name = "Skill API", useMetadata = true)
public final class SkillAPI {
    @SidedProxy(modId = "skillapi", clientSide = "skillapi.client.SkillAPIClientProxy", serverSide = "skillapi.SkillAPIProxy")
    public static SkillAPIProxy proxy;
    public static Map<String, FMLEventChannel> channels;

    @EventHandler
    public void pre(FMLPreInitializationEvent event) {
        channels = new HashMap<String, FMLEventChannel>();
        FMLEventChannel channel;
        for (int i = 0; i < SkillPacketHandler.CHANNELS.length; i++) {
            channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(SkillPacketHandler.CHANNELS[i]);
            channel.register(new SkillPacketHandler());
            channels.put(SkillPacketHandler.CHANNELS[i], channel);
        }
        if (event.getSourceFile().getName().endsWith(".jar") && event.getSide().isClient()) {
            try {
                Class.forName("mods.mud.ModUpdateDetector").getDeclaredMethod("registerMod", ModContainer.class, String.class, String.class).invoke(null,
                        FMLCommonHandler.instance().findContainerFor(this),
                        "https://raw.github.com/GotoLink/SkillAPI/master/API_update.xml",
                        "https://raw.github.com/GotoLink/SkillAPI/master/API_changelog.md"
                );
            } catch (Throwable ignored) {
            }
        }
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        proxy.loadSkillKeyBindings();
        proxy.register();
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new SkillCommand());
    }
}
