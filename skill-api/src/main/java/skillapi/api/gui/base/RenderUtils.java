package skillapi.api.gui.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

/**
 * @author Jun
 * @date 2021/3/8.
 */
@SideOnly(Side.CLIENT)
public final class RenderUtils {
    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    public static void drawGradientRect(int left, int top, int right, int bottom, int colorTop, int colorBottom) {
        float f = (float) (colorTop >> 24 & 255) / 255.0F;
        float f1 = (float) (colorTop >> 16 & 255) / 255.0F;
        float f2 = (float) (colorTop >> 8 & 255) / 255.0F;
        float f3 = (float) (colorTop & 255) / 255.0F;
        float f4 = (float) (colorBottom >> 24 & 255) / 255.0F;
        float f5 = (float) (colorBottom >> 16 & 255) / 255.0F;
        float f6 = (float) (colorBottom >> 8 & 255) / 255.0F;
        float f7 = (float) (colorBottom & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(right, top, 0.0D);
        tessellator.addVertex(left, top, 0.0D);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(left, bottom, 0.0D);
        tessellator.addVertex(right, bottom, 0.0D);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Draw a textured rectangle at the zero point of the Z axis
     *
     * @param x      Window coordinate X
     * @param y      Window coordinate Y
     * @param u      Texture coordinate X
     * @param v      Texture coordinate Y
     * @param width  The width of the texture to be drawn
     * @param height The height of the texture to be drawn
     */
    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
        drawTexturedModalRect(x, y, u, v, width, height, width, height);
    }

    /**
     * Draw a textured rectangle at the zero point of the Z axis
     *
     * @param x      Window coordinate X
     * @param y      Window coordinate Y
     * @param u      Texture coordinate X
     * @param v      Texture coordinate Y
     * @param width  The width to be drawn in the window
     * @param height The height to be drawn in the window
     * @param uWidth The width to be drawn in the window
     * @param vHeight The height to be drawn in the texture
     */
    public static void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, int uWidth, int vHeight) {
        double f = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0, u * f, (v + vHeight) * f);
        tessellator.addVertexWithUV(x + width, y + height, 0, (u + uWidth) * f, (v + vHeight) * f);
        tessellator.addVertexWithUV(x + width, y, 0, (u + uWidth) * f, v * f);
        tessellator.addVertexWithUV(x, y, 0, u * f, v * f);
        tessellator.draw();
    }

    /**
     * Renders the specified text to the screen, center-aligned.
     */
    public static void drawCenteredString(String text, int x, int y, int color) {
        final FontRenderer fontRenderer = GuiApi.minecraft.fontRenderer;
        fontRenderer.drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
    }
}
