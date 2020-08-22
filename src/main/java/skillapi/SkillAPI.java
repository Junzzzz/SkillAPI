package skillapi;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Level;
import skillapi.common.SkillEvent;
import skillapi.common.SkillProxy;
import skillapi.event.BaseSkillEvent;
import skillapi.packets.SkillPacketHandler;
import skillapi.utils.ClassUtils;
import skillapi.utils.EventBusUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(modid = "skillapi", name = "Skill API", useMetadata = true)
public final class SkillAPI {
    public static boolean isLogicalServer = false;
    public static boolean isPhysicalServer = false;

    @SidedProxy(modId = "skillapi", clientSide = "skillapi.client.SkillClientProxy", serverSide = "skillapi.server.SkillServerProxy")
    public static SkillProxy proxy;

    @SidedProxy(modId = "skillapi", clientSide = "skillapi.client.SkillAPIClientProxy", serverSide = "skillapi.SkillAPIProxy")
    public static SkillAPIProxy oldProxy;
    public static Map<String, FMLEventChannel> channels;

    @EventHandler
    public void pre(FMLPreInitializationEvent event) {
        channels = new HashMap<String, FMLEventChannel>(16);
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
    public void init(FMLInitializationEvent event) {
        BaseSkillEvent.loadSkillEvent(this.getClass(),"skillapi.event");
        proxy.init(event);

        oldProxy.loadSkillKeyBindings();
        oldProxy.register();
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        isPhysicalServer = event.getSide().isServer();
        isLogicalServer = !isPhysicalServer;

        event.registerServerCommand(new SkillCommand());
    }
}
