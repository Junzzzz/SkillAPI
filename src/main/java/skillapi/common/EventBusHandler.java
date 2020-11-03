package skillapi.common;

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
public class EventBusHandler {
    private static final EventBus FML_EVENT_BUS;
    private static final EventBus MC_EVENT_BUS;

    private static final Method FML_REGISTER;
    private static final Method MC_REGISTER;

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

    private static boolean check(Method method, Class<?> eventClass) {
        if (!Event.class.isAssignableFrom(eventClass)) {
            FMLLog.log(Level.ERROR, new Throwable(), "Method %s has @SubscribeEvent annotation, but takes a argument that is not an Event %s", method, eventClass);
            return false;
        }
        return true;
    }

    private static boolean register(EventBus bus, Method busMethod, Object targetInstance, Method method, Class<?> eventClass) {
        ModContainer activeModContainer = Loader.instance().activeModContainer();
        if (activeModContainer == null) {
            FMLLog.log(Level.ERROR, new Throwable(), "Unable to determine registrant mod for %s. This is a critical error and should be impossible", targetInstance);
            activeModContainer = Loader.instance().getMinecraftModContainer();
        }
        if (!check(method, eventClass)) {
            return false;
        }
        try {
            busMethod.invoke(bus, eventClass, targetInstance, method, activeModContainer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            FMLLog.log(Level.ERROR, e, "Unable to determine registrant mod for %s. This is a critical error and should be impossible", targetInstance);
            return false;
        }
        return true;
    }

    public static boolean forceRegisterFmlEvent(Object targetInstance, Method method, Class<?> eventClass) {
        return register(FML_EVENT_BUS, FML_REGISTER, targetInstance, method, eventClass);
    }

    public static boolean forceRegisterMcEvent(Object targetInstance, Method method, Class<?> eventClass) {
        return register(MC_EVENT_BUS, MC_REGISTER, targetInstance, method, eventClass);
    }
}
