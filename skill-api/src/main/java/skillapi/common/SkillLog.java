package skillapi.common;

import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jun
 */
public class SkillLog {
    private static final Logger LOGGER = LogManager.getLogger("SKILL");

    public static void info(String format, Object... args) {
        log(Level.INFO, format, args);
    }

    public static void error(String format, Object... args) {
        log(Level.ERROR, format, args);
    }

    public static void error(Throwable ex, String format, Object... args) {
        log(Level.ERROR, ex, format, args);
    }

    public static void warn(String format, Object... args) {
        log(Level.WARN, format, args);
    }

    public static void warn(Throwable ex, String format, Object... args) {
        log(Level.WARN, ex, format, args);
    }

    public static void log(Level level, Throwable ex, String format, Object... data) {
        LOGGER.log(level, String.format(format, data), ex);
    }

    public static void log(Level level, String format, Object... data) {
        LOGGER.log(level, String.format(format, data));
    }
}
