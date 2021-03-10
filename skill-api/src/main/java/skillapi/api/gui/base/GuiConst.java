package skillapi.api.gui.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

/**
 * @author Jun
 * @date 2021/2/23.
 */
@SideOnly(Side.CLIENT)
public class GuiConst {
    public static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

    protected static int scaleFactor;
    protected static FontRenderer fontRenderer;

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
