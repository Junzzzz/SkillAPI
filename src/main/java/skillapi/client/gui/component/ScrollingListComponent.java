package skillapi.client.gui.component;

import lombok.Builder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import skillapi.client.CachedTexture;
import skillapi.client.gui.base.BaseComponent;
import skillapi.client.gui.base.GuiBox;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jun
 * @date 2020/11/20.
 */
public class ScrollingListComponent<T> extends BaseComponent {
    private final SliderComponent slider;
    private final SlotRenderer<T> slotRenderer;
    private final int slotHeight;
    private int movableWindowHeight;
    private List<T> dataList;

    private CachedTexture cachedTexture;

    @Builder
    public ScrollingListComponent(GuiBox guiBox, int slotHeight, List<T> data, SlotRenderer<T> renderer) {
        super(guiBox);
        Objects.requireNonNull(renderer);
        this.slotRenderer = renderer;
        this.slotHeight = slotHeight;
        this.dataList = new LinkedList<>(data);
        this.slider = SliderComponent.builder()
                .sliderBox(GuiBox.builder()
                        .x(guiBox.getRight() - 6)
                        .y(guiBox.getY())
                        .width(6)
                        .height(guiBox.getHeight())
                        .build()
                ).build();
        this.slider.setButtonHeight(guiBox.getHeight() / 2);

        // Initialize the list texture
        this.initFrameBuffer();
    }

    private void initFrameBuffer() {
        this.movableWindowHeight = (this.slotHeight * this.dataList.size() - this.guiBox.getHeight()) * getScaleFactor();
        if (this.dataList.size() == 0) {
            return;
        }
        // -2 for edge, -6 for slider
        this.cachedTexture = new CachedTexture(this.guiBox.getWidth() - 6 - 2, this.dataList.size() * this.slotHeight, true);
        this.cachedTexture.startDrawTexture();
        int i = 0;
        for (T data : this.dataList) {
            this.slotRenderer.renderSlot(data, 0, i * this.slotHeight);
            i++;
        }
        this.cachedTexture.endDrawTexture();
    }

    private void renderList() {
        final int fromY = (int) (slider.getRatio() * this.movableWindowHeight);

        final int showHeight = this.movableWindowHeight > 0 ? this.guiBox.getHeight() - 2 : this.cachedTexture.getHeight();

        this.cachedTexture.render(0, 0, 0, fromY, this.guiBox.getWidth() - 6 - 2, showHeight);
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        renderListBackground();
        renderEdgeShadow();
        if (this.movableWindowHeight > 0) {
            slider.render(mouseX, mouseY, partialTicks);
        }
        renderList();
    }

    private void renderListBackground() {
        drawGradientRect(guiBox.getLeft(), guiBox.getTop(), guiBox.getRight(), guiBox.getBottom(), 0xC0101010, 0xD0101010);
    }

    private void renderEdgeShadow() {
        final Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        byte shadowLength = 4;
        // TOP
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0x000000, 0x00);
        tessellator.addVertexWithUV(guiBox.getLeft(), guiBox.getTop() + shadowLength, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(guiBox.getRight(), guiBox.getTop() + shadowLength, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0x000000, 0xFF);
        tessellator.addVertexWithUV(guiBox.getRight(), guiBox.getTop(), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(guiBox.getLeft(), guiBox.getTop(), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        // BOTTOM
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0x000000, 0xFF);
        tessellator.addVertexWithUV(guiBox.getLeft(), guiBox.getBottom(), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(guiBox.getRight(), guiBox.getBottom(), 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0x000000, 0x00);
        tessellator.addVertexWithUV(guiBox.getRight(), guiBox.getBottom() - shadowLength, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(guiBox.getLeft(), guiBox.getBottom() - shadowLength, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    private void drawGradientRect(int left, int top, int right, int bottom, int colorTop, int colorBottom) {
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @FunctionalInterface
    public interface SlotRenderer<T> {
        /**
         * Called when rendering list item
         *
         * @param data List item
         * @param x    Slot position X
         * @param y    Slot position Y
         */
        void renderSlot(T data, int x, int y);
    }
}
