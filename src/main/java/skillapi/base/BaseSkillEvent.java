package skillapi.base;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import skillapi.Application;

/**
 * @author Jun
 * @date 2020/8/20.
 */
public abstract class BaseSkillEvent<T> {
    private boolean onServer;
    private boolean onClient;

    @SubscribeEvent
    public final void run(T event) {
        if (Application.isLogicalServer) {
            doServer(event);
            doClient(event);
        } else if (Application.isPhysicalServer) {
            doServer(event);
        } else {
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