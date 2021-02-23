package skillapi.client.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author Jun
 * @date 2021/2/23.
 */
public class GuiConst {
    private static int scaleFactor;
    private static FontRenderer fontRenderer;

    // Make sure it is initialized
    static {
        reload();
    }

    protected static void reload() {
        final Minecraft mc = Minecraft.getMinecraft();
        scaleFactor = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
        fontRenderer = mc.fontRenderer;
    }

    public static int getScaleFactor() {
        return scaleFactor;
    }

    public static FontRenderer getFontRenderer() {
        return fontRenderer;
    }
}
