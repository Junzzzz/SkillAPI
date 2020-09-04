package skillapi.common;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * @author Jun
 * @date 2020/8/30.
 */
public class SkillLog {
    public static void error(String format, Object... args) {
        FMLLog.log(Level.ERROR, format, args);
    }

    public static void error(String format, Throwable ex, Object... args) {
        FMLLog.log(Level.ERROR, ex, format, args);
    }

    public static void warn(String format, Object... args) {
        FMLLog.log(Level.WARN, format, args);
    }

    public static void warn(String format, Throwable ex, Object... args) {
        FMLLog.log(Level.WARN, ex, format, args);
    }
}
