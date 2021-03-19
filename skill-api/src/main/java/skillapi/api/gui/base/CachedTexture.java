package skillapi.api.gui.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

/**
 * @author Jun
 * @date 2021/2/14.
 */
@SideOnly(Side.CLIENT)
public class CachedTexture {
    private final Framebuffer framebuffer;
    private final int width;
    private final int height;
    private final int scaleFactor;

    private boolean hasTexture = false;
    private boolean delFlag = false;

    public CachedTexture(int width, int height) {
        this.width = width;
        this.height = height;
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        this.scaleFactor = scaledResolution.getScaleFactor();
        if (checkBounds()) {
            this.framebuffer = null;
        } else {
            this.framebuffer = new Framebuffer(width * this.scaleFactor, height * this.scaleFactor, false);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**
     * Start drawing the cache texture. Note that the image should be drawn from the origin (0, 0)
     */
    public void startDrawTexture() {
        if (delFlag) {
            throw new RuntimeException("This texture has been deleted, please recreate it！");
        }
        this.hasTexture = false;
        if (checkBounds()) {
            return;
        }
        this.framebuffer.bindFramebuffer(true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, this.width, this.height, 0.0D, 100.0D, 300.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -200.0F);

        if (!OpenGlHelper.isFramebufferEnabled()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        }
    }

    public void endDrawTexture() {
        if (checkBounds()) {
            return;
        }
        this.framebuffer.unbindFramebuffer();
        this.hasTexture = true;
    }

    private boolean checkBounds() {
        return this.width == 0 || this.height == 0;
    }

    public void bindTexture() {
        if (this.framebuffer != null) {
            this.framebuffer.bindFramebufferTexture();
        }
    }

    public void unbindTexture() {
        if (this.framebuffer != null) {
            this.framebuffer.unbindFramebufferTexture();
        }
    }

    public void render(Layout layout) {
        render(layout.x, layout.y, 0, 0, layout.width, layout.height, true);
    }

    public void render(int x, int y, int width, int height) {
        render(x, y, 0, 0, width, height, true);
    }

    public void render(int x, int y, int textureX, int textureY, int width, int height) {
        render(x, y, textureX, textureY, width, height, true);
    }

    public void render(int x, int y, int textureX, int textureY, int width, int height, boolean blend) {
        if (!hasTexture || this.framebuffer == null) {
            return;
        }
        GL11.glColorMask(true, true, true, false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        if (blend) {
            GL11.glEnable(GL11.GL_BLEND);
        } else {
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        // Flip
        textureY = this.height * this.scaleFactor - textureY;

        // Calculate u, v
        final double u0 = (double) textureX / this.framebuffer.framebufferTextureWidth;
        final double v0 = (double) textureY / this.framebuffer.framebufferTextureHeight;
        final double u1 = (double) (textureX + width * this.scaleFactor) / this.framebuffer.framebufferTextureWidth;
        // Flip
        final double v1 = (double) (textureY - height * this.scaleFactor) / this.framebuffer.framebufferTextureHeight;
        this.framebuffer.bindFramebufferTexture();

        // Render
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0.0D, u0, v1);
        tessellator.addVertexWithUV(x + width, y + height, 0.0D, u1, v1);
        tessellator.addVertexWithUV(x + width, y, 0.0D, u1, v0);
        tessellator.addVertexWithUV(x, y, 0.0D, u0, v0);
        tessellator.draw();

        this.framebuffer.unbindFramebufferTexture();
        GL11.glDepthMask(true);
        GL11.glColorMask(true, true, true, true);
    }

    public void clear() {
        if (this.framebuffer != null) {
            this.framebuffer.framebufferClear();
        }
    }

    public void delete() {
        if (!delFlag) {
            if (this.framebuffer != null) {
                this.framebuffer.deleteFramebuffer();
            }
            this.delFlag = true;
        }
    }

    @Override
    protected void finalize() {
        delete();
    }
}
