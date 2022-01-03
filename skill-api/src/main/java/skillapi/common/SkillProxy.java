package skillapi.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Jun
 */
public interface SkillProxy {
    /**
     * 初始化前
     *
     * @param event 事件
     */
    void preInit(FMLPreInitializationEvent event);

    /**
     * 初始化
     *
     * @param event 事件
     */
    void init(FMLInitializationEvent event);

    /**
     * MOD间交互
     *
     * @param event 事件
     */
    void postInit(FMLPostInitializationEvent event);
}
