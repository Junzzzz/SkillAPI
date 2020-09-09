package skillapi.event.base;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import skillapi.Application;

/**
 * @author Jun
 * @date 2020/8/20.
 */
public abstract class BaseSkillEvent<T extends Event> {
    private boolean onServer;
    private boolean onClient;

    @SubscribeEvent
    public final void run(T event) {
        if (Application.isPhysicalServer) {
            doServer(event);
        } else if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                doServer(event);
            } else {
                doClient(event);
            }
        }  else {
            doClient(event);
        }
    }

    private void doServer(T e) {
        if (onServer) {
            onServer(e);
        }
    }

    private void doClient(T e) {
        if (onClient) {
            onClient(e);
        }
    }

    /**
     * 服务端事件
     *
     * @param event 目标事件
     */
    protected abstract void onServer(T event);

    /**
     * 客户端事件
     *
     * @param event 目标事件
     */
    protected abstract void onClient(T event);
}