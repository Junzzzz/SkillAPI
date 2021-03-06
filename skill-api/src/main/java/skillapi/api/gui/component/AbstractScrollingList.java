package skillapi.api.gui.component;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.Layout;
import skillapi.client.CachedTexture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jun
 * @date 2021/2/24.
 */
public abstract class AbstractScrollingList<T> extends BaseComponent {
    private CachedTexture cachedTexture;
    private final SliderComponent slider;

    private final Layout elementsBox;
    private final int slotHeight;
    private int movableWindowHeight;
    private final ArrayList<T> dataList;

    protected int selectedIndex = -1;

    public AbstractScrollingList(Layout layout, int slotHeight, List<T> data, boolean lazyLoad) {
        super(layout);
        this.slotHeight = slotHeight;

        this.dataList = new ArrayList<>(data);
        this.slider = SliderComponent.builder()
                .sliderBox(Layout.builder()
                        .x(layout.getRight() - 6)
                        .y(layout.getY())
                        .width(6)
                        .height(layout.getHeight())
                        .build()
                ).build();
        // -2 for edge, -6 for slider
        this.elementsBox = Layout.builder()
                .x(layout.getX() + 1)
                .y(layout.getY() + 1)
                .width(layout.getWidth() - 2)
                .height(layout.getHeight() - 2).build();
        setSliderButtonHeight();

        // Initialize the list texture
        if (!lazyLoad) {
            this.init();
        }
    }

    public AbstractScrollingList(Layout layout, int slotHeight, List<T> data) {
        this(layout, slotHeight, data, false);
    }

    /**
     * Render every line of graphics
     *
     * @param data List item
     * @param x    Starting coordinate X
     * @param y    Starting coordinate Y
     */
    protected abstract void renderSlot(T data, int x, int y);

    /**
     * Called when the element is clicked
     *
     * @param index Element index
     */
    protected abstract void elementClicked(int index);

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public T getSelected() {
        if (hasSelected()) {
            return this.dataList.get(this.selectedIndex);
        }
        return null;
    }

    public T removeSelected() {
        if (!hasSelected()) {
            return null;
        }
        final T tmp = this.dataList.remove(this.selectedIndex);
        if (this.selectedIndex >= this.dataList.size()) {
            this.selectedIndex--;
        }
        refresh();
        return tmp;
    }

    public boolean hasSelected() {
        return this.dataList.size() > selectedIndex && selectedIndex > -1;
    }

    public void add(T item) {
        this.dataList.add(item);
        refresh();
    }

    public void refresh() {
        setSliderButtonHeight();
        refreshCachedTexture();
    }

    protected List<T> getList() {
        return Collections.unmodifiableList(this.dataList);
    }

    public int getListSize() {
        return this.dataList.size();
    }

    protected void init() {
        refreshCachedTexture();
    }

    private void refreshCachedTexture() {
        this.movableWindowHeight = getContentHeight() - this.layout.getHeight();
        this.elementsBox.setWidth(layout.getWidth() - 2 - (this.movableWindowHeight > 0 ? 6 : 0));
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
            renderSelected(0, this.selectedIndex * this.slotHeight);
        }

        int i = 0;
        for (T data : this.dataList) {
            this.renderSlot(data, 1, i * this.slotHeight);
            i++;
        }
        texture.endDrawTexture();
        return texture;
    }

    private int getContentHeight() {
        return this.slotHeight * this.dataList.size();
    }

    private void setSliderButtonHeight() {
        int height = this.layout.getHeight() * this.layout.getHeight();

        if (this.dataList.size() > 0) {
            height /= this.getContentHeight();
        }

        if (height < 32) {
            height = 32;
        }

        if (height > this.layout.getHeight() - 8) {
            height = this.layout.getHeight() - 8;
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
        elementClicked(this.selectedIndex);
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
        drawGradientRect(layout.getLeft(), layout.getTop(), layout.getRight(), layout.getBottom(), 0xC0101010, 0xD0101010);
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
        tessellator.addVertexWithUV(layout.getLeft(), layout.getTop() + shadowLength, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(layout.getRight(), layout.getTop() + shadowLength, 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0x000000, 0xFF);
        tessellator.addVertexWithUV(layout.getRight(), layout.getTop(), 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(layout.getLeft(), layout.getTop(), 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        // BOTTOM
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0x000000, 0xFF);
        tessellator.addVertexWithUV(layout.getLeft(), layout.getBottom(), 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(layout.getRight(), layout.getBottom(), 0.0D, 1.0D, 1.0D);
        tessellator.setColorRGBA_I(0x000000, 0x00);
        tessellator.addVertexWithUV(layout.getRight(), layout.getBottom() - shadowLength, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(layout.getLeft(), layout.getBottom() - shadowLength, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
    }

    private void renderSelected(int x, int y) {
        final int x1 = x + this.elementsBox.getWidth();
        final int y1 = y + this.slotHeight;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x808080);
        tessellator.addVertexWithUV(x, y1, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x1, y1, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(x1, y, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
        tessellator.setColorOpaque_I(0);
        tessellator.addVertexWithUV(x + 1, y1 - 1, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(x1 - 1, y1 - 1, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(x1 - 1, y + 1, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y + 1, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void renderList() {
        final int y = (int) (slider.getRatio() * this.movableWindowHeight);
        final int fromY = y * getScaleFactor();

        final int showHeight = this.movableWindowHeight > 0 ? this.layout.getHeight() - 2 : this.cachedTexture.getHeight();

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
