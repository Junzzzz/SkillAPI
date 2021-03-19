package skillapi.api.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.input.Keyboard;

/**
 * @author Jun
 * @date 2021/3/8.
 */
public abstract class GenericGui {
    /**
     * Render function
     *
     * @param mouseX       Mouse x axis
     * @param mouseY       Mouse y axis
     * @param partialTicks Time tick
     */
    protected abstract void render(int mouseX, int mouseY, float partialTicks);

    /**
     * Get text renderer
     *
     * @return Renderer
     */
    protected final FontRenderer getFontRenderer() {
        return GuiApi.minecraft.fontRenderer;
    }

    /**
     * Get the game sound controller
     *
     * @return Sound handler
     */
    protected final SoundHandler getSoundHandler() {
        return GuiApi.minecraft.getSoundHandler();
    }

    /**
     * Get window zoom ratio
     *
     * @return The number of the ratio
     */
    protected final int getScaleFactor() {
        return GuiApi.scaleFactor;
    }

    /**
     * @return The minecraft!
     * @see net.minecraft.client.Minecraft
     */
    protected final Minecraft getMinecraft() {
        return GuiApi.minecraft;
    }

    /**
     * The RenderEngine instance used by Minecraft
     */
    protected final TextureManager getTextureManager() {
        return GuiApi.minecraft.renderEngine;
    }
}
