package skillapi.utils;

import net.minecraft.server.MinecraftServer;
import skillapi.skill.Skills;

import java.io.File;

/**
 * @author Jun
 */
public final class SkillServer {
    private static MinecraftServer server;

    public static void init(MinecraftServer server) {
        SkillServer.server = server;
        SkillNBT.init();
        Skills.init();
    }

    public static File getWorldDirectory() {
        return server.getActiveAnvilConverter().getSaveLoader(server.getFolderName(), false).getWorldDirectory();
    }
}
