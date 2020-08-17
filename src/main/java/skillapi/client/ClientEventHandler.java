package skillapi.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class ClientEventHandler {
    public static final ClientEventHandler INSTANCE = new ClientEventHandler();
    private final HudSkills skillsHUD = new HudSkills();

    private ClientEventHandler() {
    }

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if (event.type == ElementType.AIR) {
            skillsHUD.drawHUD(event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), event.partialTicks);
            Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
        }
    }
}
