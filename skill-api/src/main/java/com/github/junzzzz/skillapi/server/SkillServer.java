package com.github.junzzzz.skillapi.server;

import com.github.junzzzz.skillapi.common.SkillNBT;
import com.github.junzzzz.skillapi.skill.SkillExecutor;
import com.github.junzzzz.skillapi.skill.Skills;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.storage.SaveFormatOld;

import java.io.File;
import java.util.List;

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

    public static ServerConfigurationManager getManager() {
        return server.getConfigurationManager();
    }

    @SuppressWarnings("unchecked")
    public static List<EntityPlayerMP> getPlayerList() {
        return (List<EntityPlayerMP>) getManager().playerEntityList;
    }

    public static void kick(EntityPlayer player, String message) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer(message);
        }
    }

    public static boolean hasHighestAuthority(EntityPlayer player) {
        return server.getConfigurationManager().func_152596_g(player.getGameProfile());
    }
}
