package io.github.junzzzz.skillapi;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import io.github.junzzzz.skillapi.common.SkillProxy;
import io.github.junzzzz.skillapi.server.PacketInjection;
import io.github.junzzzz.skillapi.server.SkillServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.sf.cglib.proxy.Enhancer;

@Mod(modid = Application.MOD_ID, name = "Skill API", useMetadata = true)
public final class Application {
    public static final String PACKAGE_PREFIX = "io.github.junzzzz.";
    public static final String MOD_ID = "skillapi";
    public static boolean isPhysicalServer = FMLLaunchHandler.side().isServer();

    @SidedProxy(modId = MOD_ID, clientSide = PACKAGE_PREFIX + "skillapi.client.SkillClientProxy", serverSide = PACKAGE_PREFIX + "skillapi.server.SkillServerProxy")
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
}
