package skillapi.api.gui.base;

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
     * Get window zoom ratio
     *
     * @return The number of the ratio
     */
    static int getScaleFactor() {
        return GuiConst.scaleFactor;
    }
}
