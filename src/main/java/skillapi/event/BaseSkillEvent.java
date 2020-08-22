package skillapi.event;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import skillapi.SkillAPI;
import skillapi.common.SkillEvent;
import skillapi.utils.ClassUtils;
import skillapi.utils.EventBusUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Jun
 * @date 2020/8/20.
 */
public abstract class BaseSkillEvent<T> {
    @SubscribeEvent
    public final void run(T event) {
        if (SkillAPI.isLogicalServer) {
            doServer(event);
            doClient(event);
        } else if (SkillAPI.isPhysicalServer) {
            doServer(event);
        } else {
            doClient(event);
        }
    }

    /**
     * 服务端事件
     *
     * @param event 事件
     */
    abstract void doServer(T event);

    /**
     * 客户端事件
     *
     * @param event 事件
     */
    abstract void doClient(T event);

    public static void loadSkillEvent(Class<?> loadClass, String packageName) {
        final List<Class<?>> classes = ClassUtils.scanLocalPackageClasses(loadClass, packageName);
        for (Class<?> cls : classes) {
            if (cls.getAnnotation(SkillEvent.class) == null) {
                continue;
            }

            final Type superClass = cls.getGenericSuperclass();
            if (superClass instanceof ParameterizedType) {
                final Type[] types = ((ParameterizedType) superClass).getActualTypeArguments();
                if (types.length != 1) {
                    continue;
                }
                final Class<?> eventClass = (Class<?>) types[0];
                final String eventName = eventClass.getName();
                final String eventPackage = ClassUtils.getClassPackage(eventName);

                for (Method method : cls.getMethods()) {
                    if (!method.isAnnotationPresent(SubscribeEvent.class)) {
                        continue;
                    }
                    if (eventPackage.startsWith("cpw.mods.fml.common.gameevent")) {
                        if (!EventBusUtils.forceRegisterFMLEvent(cls, method, eventClass)) {
                            FMLLog.log(Level.ERROR, "FML event registration failed. From: %s.%s", cls.getName(), method.getName());
                        }
                    } else if (eventPackage.startsWith("net.minecraftforge.event")) {
                        if (!EventBusUtils.forceRegisterMCEvent(cls, method, eventClass)) {
                            FMLLog.log(Level.ERROR, "Minecraft event registration failed From: %s.%s", cls.getName(), method.getName());
                        }
                    }
                }
            }
        }
    }
}