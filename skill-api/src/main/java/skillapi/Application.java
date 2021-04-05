package skillapi;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import skillapi.api.SkillApi;
import skillapi.common.SkillProxy;
import skillapi.packets.SkillPacketHandler;
import skillapi.skill.SkillHandler;
import skillapi.skill.SkillLocalConfig;

import java.util.HashMap;
import java.util.Map;

@Mod(modid = Application.MOD_ID, name = "Skill API", useMetadata = true)
public final class Application {
    public static final String MOD_ID = "skillapi";
    public static boolean isPhysicalServer = FMLLaunchHandler.side().isServer();

    @SidedProxy(modId = MOD_ID, clientSide = "skillapi.client.SkillClientProxy", serverSide = "skillapi.server.SkillServerProxy")
    public static SkillProxy proxy;

    @SidedProxy(modId = MOD_ID, clientSide = "skillapi.client.SkillAPIClientProxy", serverSide = "skillapi.SkillAPIProxy")
    public static SkillAPIProxy oldProxy;
    public static Map<String, FMLEventChannel> channels;

    @EventHandler
    public void pre(FMLPreInitializationEvent event) {
        proxy.preInit(event);

        SkillLocalConfig.load(event);

        SkillApi.init(event);
        channels = new HashMap<>(16);
        FMLEventChannel channel;
        for (int i = 0; i < SkillPacketHandler.CHANNELS.length; i++) {
            channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(SkillPacketHandler.CHANNELS[i]);
            channel.register(new SkillPacketHandler());
            channels.put(SkillPacketHandler.CHANNELS[i], channel);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);

        oldProxy.loadSkillKeyBindings();
        oldProxy.register();
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        SkillHandler.register(SkillLocalConfig.SERVER_CONFIG);
        event.registerServerCommand(new SkillCommand());
    }
}
