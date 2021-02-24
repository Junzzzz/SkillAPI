package skillapi.api.gui.component;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.GuiBox;
import skillapi.client.CachedTexture;

import java.util.List;

/**
 * @author Jun
 * @date 2021/2/24.
 */
public abstract class AbstractScrollingList<T> extends BaseComponent {
    private CachedTexture cachedTexture;
    //    private CachedTexture cachedSelectTexture;
    private final SliderComponent slider;

    private final GuiBox elementsBox;
    private final int slotHeight;
    private int movableWindowHeight;
    private final List<T> dataList;

    private int selectedIndex = -1;

    public AbstractScrollingList(GuiBox guiBox, int slotHeight, List<T> data, boolean lazyLoad) {
        super(guiBox);
        this.slotHeight = slotHeight;
        this.dataList = data;
        this.slider = SliderComponent.builder()
                .sliderBox(GuiBox.builder()
                        .x(guiBox.getRight() - 6)
                        .y(guiBox.getY())
                        .width(6)
                        .height(guiBox.getHeight())
                        .build()
                ).build();
        // -2 for edge, -6 for slider
        this.elementsBox = GuiBox.builder()
                .x(guiBox.getX() + 1)
                .y(guiBox.getY() + 1)
                .width(guiBox.getWidth() - 6 - 2)
                .height(guiBox.getHeight() - 2).build();
        setSliderButtonHeight();

        // Initialize the list texture
        if (!lazyLoad) {
            this.init();
        }
    }

    public AbstractScrollingList(GuiBox guiBox, int slotHeight, List<T> data) {
        this(guiBox, slotHeight, data, false);
    }

    /**
     * Render every line of graphics
     *
     * @param data List item
     * @param x    Starting coordinate X
     * @param y    Starting coordinate Y
     */
    protected abstract void renderSlot(T data, int x, int y);

    public void add(T item) {
        this.dataList.add(item);
        setSliderButtonHeight();
        refreshCachedTexture();
    }

    protected void init() {
//        this.cachedSelectTexture = new CachedTexture(elementsBox.getWidth() + 1, this.slotHeight, true);
//        this.cachedSelectTexture.startDrawTexture();
//        renderSelected(0, 0);
//        this.cachedSelectTexture.endDrawTexture();
        refreshCachedTexture();
    }

    private void refreshCachedTexture() {
        this.movableWindowHeight = getContentHeight() - this.guiBox.getHeight();
        final CachedTexture temp = this.cachedTexture;
        this.cachedTexture = createCachedTexture();

        if (temp != null) {
            temp.delete();
        }
    }

    private CachedTexture createCachedTexture() {
        CachedTexture texture = new CachedTexture(elementsBox.getWidth(), getContentHeight(), true);
        texture.startDrawTexture();

        if (this.selectedIndex > -1 && this.selectedIndex < this.dataList.size()) {
            renderSelected(0, this.selectedIndex * this.slotHeight + 2);
        }

        int i = 0;
        for (T data : this.dataList) {
            this.renderSlot(data, 0, i * this.slotHeight);
            i++;
        }
        texture.endDrawTexture();
        return texture;
    }

    private int getContentHeight() {
        return this.slotHeight * this.dataList.size();
    }

    private void setSliderButtonHeight() {
        int height = this.guiBox.getHeight() * this.guiBox.getHeight();

        if (this.dataList.size() > 0) {
            height /= this.getContentHeight();
        }

        if (height < 32) {
            height = 32;
        }

        if (height > this.guiBox.getHeight() - 8) {
            height = this.guiBox.getHeight() - 8;
        }
        this.slider.setButtonHeight(height);
    }


    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        renderListBackground();
        renderEdgeShadow();
        if (this.movableWindowHeight > 0) {
            slider.render(mouseX, mouseY, partialTicks);
        }
        if (this.dataList.size() > 0) {
            renderList();
        }
    }

    @Override
    protected boolean mouseReleased(int mouseX, int mouseY) {
        slider.mouseReleased(mouseX, mouseY);

        if (!this.elementsBox.isInBox(mouseX, mouseY)) {
            return false;
        }
        this.selectedIndex = ((int) (slider.getRatio() * this.movableWindowHeight) + mouseY - this.elementsBox.getY()) / this.slotHeight;
        refreshCachedTexture();
        return true;
    }

    @Override
    protected boolean mousePressed(int mouseX, int mouseY) {
        if (slider.mousePressed(mouseX, mouseY)) {
            return true;
        }
        return false;
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

    private void renderSelected(int x, int y) {
        final int x1 = x + this.elementsBox.getWidth();
        final int y1 = y + this.slotHeight - 3;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x808080);
        tessellator.addVertexWithUV(x, y1 + 2, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x1, y1 + 2, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(x1, y - 2, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y - 2, 0.0D, 0.0D, 0.0D);
        tessellator.setColorOpaque_I(0);
        tessellator.addVertexWithUV(x + 1, y1 + 1, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x1 - 1, y1 + 1, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(x1 - 1, y - 1, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y - 1, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void renderList() {
        final int y = (int) (slider.getRatio() * this.movableWindowHeight);
        final int fromY = y * getScaleFactor();

        final int showHeight = this.movableWindowHeight > 0 ? this.guiBox.getHeight() - 2 : this.cachedTexture.getHeight();

//        // Render selected
//        if (this.selectedIndex > -1 && this.selectedIndex < this.dataList.size()) {
//            final int start = y / this.slotHeight;
//            final int end = (y + showHeight) / this.slotHeight;
//            if (start <= this.selectedIndex && this.selectedIndex <= end) {
//                int paddingY = (this.selectedIndex - start) * this.slotHeight;
//                int textureY = 0;
//                int textureHeight = this.cachedSelectTexture.getHeight();
//                // Start overlapping
//                if (paddingY == 0) {
//                    textureY = y - this.slotHeight * start;
//                    textureHeight = this.slotHeight - textureY;
//                }
//                // End overlapping
//                if (this.selectedIndex == end) {
//                    textureHeight = this.slotHeight - this.guiBox.getBottom() + elementsBox.getY() + paddingY;
//                }
//                this.cachedSelectTexture.render(elementsBox.getX(),
//                        elementsBox.getY() + paddingY,
//                        0,
//                        textureY,
//                        this.cachedSelectTexture.getWidth(),
//                        textureHeight);
//                this.getFontRenderer().drawString("start: " + start, this.guiBox.getRight() + 10, 20, 0xFFFFFF);
//                this.getFontRenderer().drawString("end: " + end, this.guiBox.getRight() + 10, 40, 0xFFFFFF);
//                this.getFontRenderer().drawString("selectIndex: " + this.selectedIndex, this.guiBox.getRight() + 10, 60, 0xFFFFFF);
//                this.getFontRenderer().drawString("paddingY: " + paddingY, this.guiBox.getRight() + 10, 80, 0xFFFFFF);
//                this.getFontRenderer().drawString("textureHeight: " + textureHeight, this.guiBox.getRight() + 10, 100, 0xFFFFFF);
//            }
//        }

        // Render elements
        this.cachedTexture.render(elementsBox.getX(),
                elementsBox.getY(),
                0,
                fromY,
                this.cachedTexture.getWidth(),
                showHeight);
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
}
