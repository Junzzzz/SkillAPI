package skillapi.base;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import skillapi.Application;

/**
 * @author Jun
 * @date 2020/8/20.
 */
public abstract class BaseSkillEvent<T> {
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

    /**
     * 服务端事件
     *
     * @param event 目标事件
     */
    public abstract void doServer(T event);

    /**
     * 客户端事件
     *
     * @param event 目标事件
     */
    public abstract void doClient(T event);
}