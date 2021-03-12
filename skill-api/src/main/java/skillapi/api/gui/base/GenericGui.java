package skillapi.api.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;

/**
 * @author Jun
 * @date 2021/3/8.
 */
public interface GenericGui {
    /**
     * Get text renderer
     *
     * @return Renderer
     */
    default FontRenderer getFontRenderer() {
        return GuiConst.fontRenderer;
    }

    /**
     *
     * @return
     */
    default SoundHandler getSoundHandler() {
        return Minecraft.getMinecraft().getSoundHandler();
    }

    /**
     * Get window zoom ratio
     *
     * @return The number of the ratio
     */
    static int getScaleFactor() {
        return GuiConst.scaleFactor;
    }
}
