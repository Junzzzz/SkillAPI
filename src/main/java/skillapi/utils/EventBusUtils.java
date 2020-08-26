package skillapi.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import skillapi.skill.SkillRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Jun
 * @date 2020/8/22.
 */
public class EventBusUtils {
    private static EventBus FML_EVENT_BUS;
    private static EventBus MC_EVENT_BUS;

    private static Method FML_REGISTER;
    private static Method MC_REGISTER;

    static {
        try {
            FML_EVENT_BUS = FMLCommonHandler.instance().bus();
            FML_REGISTER = FML_EVENT_BUS.getClass().getDeclaredMethod("register", Class.class, Object.class, Method.class, ModContainer.class);
            FML_REGISTER.setAccessible(true);

            MC_EVENT_BUS = MinecraftForge.EVENT_BUS;
            MC_REGISTER = MC_EVENT_BUS.getClass().getDeclaredMethod("register", Class.class, Object.class, Method.class, ModContainer.class);
            MC_REGISTER.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new SkillRuntimeException("Can't find Forge event class");
        }
    }

    private static boolean check(Method method, Class<?> eventType) {
        if (!Event.class.isAssignableFrom(eventType)) {
            FMLLog.log(Level.ERROR, new Throwable(), "Method %s has @SubscribeEvent annotation, but takes a argument that is not an Event %s", method, eventType);
            return false;
        }
        return true;
    }

    private static boolean register(EventBus bus, Method busMethod, Class<?> target, Method method, Class<?> eventType) {
        ModContainer activeModContainer = Loader.instance().activeModContainer();
        if (activeModContainer == null) {
            FMLLog.log(Level.ERROR, new Throwable(), "Unable to determine registrant mod for %s. This is a critical error and should be impossible", target);
            activeModContainer = Loader.instance().getMinecraftModContainer();
        }
        if (!check(method, eventType)) {
            return false;
        }
        try {
            busMethod.invoke(bus, eventType, target.newInstance(), method, activeModContainer);
        } catch (IllegalAccessException e) {
            FMLLog.log(Level.ERROR, e, "Unable to determine registrant mod for %s. This is a critical error and should be impossible", target);
            return false;
        } catch (InvocationTargetException e) {
            FMLLog.log(Level.ERROR, e, "Unable to determine registrant mod for %s. This is a critical error and should be impossible", target);
            return false;
        } catch (InstantiationException e) {
            FMLLog.log(Level.ERROR, e, "Unable to determine registrant mod for %s. This is a critical error and should be impossible", target);
            return false;
        }
        return true;
    }

    public static boolean forceRegisterFMLEvent(Class<?> target, Method method, Class<?> eventType) {
        return register(FML_EVENT_BUS, FML_REGISTER, target, method, eventType);
    }
    public static boolean forceRegisterMCEvent(Class<?> target, Method method, Class<?> eventType) {
        return register(MC_EVENT_BUS, MC_REGISTER, target, method, eventType);
    }
}
