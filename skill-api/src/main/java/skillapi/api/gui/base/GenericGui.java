package skillapi.api.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;

/**
 * @author Jun
 * @date 2021/3/8.
 */
public abstract class GenericGui {
    /**
     * Get text renderer
     *
     * @return Renderer
     */
    public FontRenderer getFontRenderer() {
        return GuiConst.fontRenderer;
    }

    /**
     * Get the game sound controller
     *
     * @return Sound handler
     */
    protected SoundHandler getSoundHandler() {
        return Minecraft.getMinecraft().getSoundHandler();
    }

    /**
     * Get window zoom ratio
     *
     * @return The number of the ratio
     */
    protected int getScaleFactor() {
        return GuiConst.scaleFactor;
    }
}
