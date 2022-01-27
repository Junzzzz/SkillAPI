package skillapi.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.SaveFormatOld;
import skillapi.common.SkillNBT;
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

    public static long getTotalTime() {
        return server.getEntityWorld().getTotalWorldTime();
    }

    public static void kick(EntityPlayer player, String message) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer(message);
        }
    }
}
