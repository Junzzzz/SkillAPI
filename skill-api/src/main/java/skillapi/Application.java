package skillapi;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.server.management.ServerConfigurationManager;
import net.sf.cglib.proxy.Enhancer;
import skillapi.common.SkillProxy;
import skillapi.server.PacketInjection;
import skillapi.server.SkillServer;

@Mod(modid = Application.MOD_ID, name = "Skill API", useMetadata = true)
public final class Application {
    public static final String MOD_ID = "skillapi";
    public static boolean isPhysicalServer = FMLLaunchHandler.side().isServer();

    @SidedProxy(modId = MOD_ID, clientSide = "skillapi.client.SkillClientProxy", serverSide = "skillapi.server.SkillServerProxy")
    public static SkillProxy proxy;

    @EventHandler
    public void pre(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        SkillServer.init(event.getServer());
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

    @EventHandler
    public void remap(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping missingMapping : event.get()) {
            if (missingMapping.type == GameRegistry.Type.ITEM) {
                missingMapping.remap(GameData.getItemRegistry().getObject(missingMapping.name.replace(" ", "")));
            }
        }
    }
}
