package skillapi;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.server.management.ServerConfigurationManager;
import net.sf.cglib.proxy.Enhancer;
import skillapi.api.SkillApi;
import skillapi.common.SkillProxy;
import skillapi.packets.SkillPacketHandler;
import skillapi.server.PacketInjection;
import skillapi.utils.SkillServer;

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
        SkillApi.preInit(event);
        proxy.preInit(event);

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
        SkillApi.init(event);
        proxy.init(event);

        oldProxy.loadSkillKeyBindings();
        oldProxy.register();
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        SkillServer.init(event.getServer());
        event.registerServerCommand(new SkillCommand());
    }

    @EventHandler
    public void serverProxy(FMLServerAboutToStartEvent event) {
        ServerConfigurationManager manager = event.getServer().getConfigurationManager();
        Enhancer enhancer = new Enhancer();
        PacketInjection helper = new PacketInjection(manager.getClass());
        enhancer.setSuperclass(manager.getClass());
        enhancer.setCallbackFilter(helper);
        enhancer.setCallbacks(helper.getCallbacks());
        Class<?> parameterType = manager.getClass().getDeclaredConstructors()[0].getParameterTypes()[0];
        ServerConfigurationManager proxy = (ServerConfigurationManager) enhancer.create(
                new Class[]{parameterType}, new Object[]{event.getServer()}
        );
        event.getServer().func_152361_a(proxy);
    }
}
