package skillapi.event.base;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import skillapi.Application;
import skillapi.common.SkillLog;

/**
 * Used to distinguish the logic execution side of the code.
 * This makes it easier to develop code.
 * If you do not need the code to clearly distinguish between the server and the client, please do not inherit this class
 *
 * @author Jun
 * @date 2020/8/20.
 */
public abstract class AbstractSkillEvent<T extends Event> {
    private boolean onServer;
    private boolean onClient;

    @SubscribeEvent
    public final void run(T event) {
        // Check if only execute on the server
        if (Application.isPhysicalServer) {
            doServer(event);
            return;
        }

        // Check inner server running
        if (this.onServer && Minecraft.getMinecraft().isIntegratedServerRunning()) {
            if (!onClient) {
                // Only happens on server
                onServer(event);
            } else if (isServer(event)) {
                // Server thread
                onServer(event);
            } else {
                // Client thread
                onClient(event);
            }
            return;
        }

        // Only execute on the client
        doClient(event);
    }

    /**
     * You'd better override this function
     *
     * @param event Pending event
     * @return If it is an event executed by the server, return {@code true} otherwise return {@code false}
     */
    protected boolean isServer(T event) {
        if (event instanceof net.minecraftforge.event.entity.player.PlayerEvent) {
            return !((net.minecraftforge.event.entity.player.PlayerEvent) event).entityPlayer.worldObj.isRemote;
        }
        if (event instanceof cpw.mods.fml.common.gameevent.PlayerEvent) {
            return !((cpw.mods.fml.common.gameevent.PlayerEvent) event).player.worldObj.isRemote;
        }
        if (event instanceof EntityEvent) {
            return !((EntityEvent) event).entity.worldObj.isRemote;
        }
        if (event instanceof BlockEvent) {
            return !((BlockEvent) event).world.isRemote;
        }
        if (event instanceof WorldEvent) {
            return !((WorldEvent) event).world.isRemote;
        }

        final String className = event.getClass().getName();

        if (className.startsWith("net.minecraftforge.client.event")) {
            return false;
        }
        SkillLog.warn("Don't hit. This may affect processing efficiency. Class: " + className);
        // The worst situation
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    private void doServer(T e) {
        if (onServer) {
            onServer(e);
        }
    }

    private void doClient(T e) {
        if (onClient) {
            onClient(e);
        }
    }

    /**
     * Code executed only on the server
     *
     * @param event Target event
     */
    protected abstract void onServer(T event);

    /**
     * Code executed only on the client
     *
     * @param event Target event
     */
    protected abstract void onClient(T event);
}