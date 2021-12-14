package skillapi.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveFormatOld;
import skillapi.skill.SkillExecutor;
import skillapi.skill.Skills;

import java.io.File;

/**
 * @author Jun
 */
public final class SkillServer {
    private static MinecraftServer server;

    public static void init(MinecraftServer server) {
        SkillServer.server = server;
        SkillExecutor.init();
        SkillNBT.init();
        Skills.init();
    }

    public static File getWorldDirectory() {
        return new File(((SaveFormatOld) server.getActiveAnvilConverter()).savesDirectory, server.getFolderName());
    }
}
