package com.github.junzzzz.skillapi.api.gui.component;

import com.github.junzzzz.skillapi.api.gui.base.*;
import com.github.junzzzz.skillapi.api.gui.base.listener.FocusChangedListener;
import com.github.junzzzz.skillapi.api.gui.base.listener.MousePressedListener;
import com.github.junzzzz.skillapi.api.gui.base.listener.MouseReleasedListener;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jun
 */
public abstract class AbstractScrollingListComponent<T> extends BaseComponent {
    protected boolean _itemClick = false;
    private CachedTexture cachedTexture;
    protected final SliderComponent slider;

    protected final Layout elementsLayout;
    protected final int slotHeight;
    protected int movableWindowHeight;
    protected int selectedIndex = -1;
    protected final ArrayList<T> dataList;

    public AbstractScrollingListComponent(Layout layout, int slotHeight, List<T> data, boolean lazyInit) {
        super(layout);
        this.slotHeight = slotHeight;

        this.dataList = new ArrayList<>(data);
        this.slider = new SliderComponent(Layout.builder()
                .x(layout.getRight() - 6)
                .y(layout.getY())
                .width(6)
                .height(layout.getHeight())
                .build()
        );
        addComponent(this.slider);

        // -2 for edge, -6 for slider
        this.elementsLayout = Layout.builder()
                .x(layout.getX() + 1)
                .y(layout.getY() + 1)
                .width(layout.getWidth() - 2)
                .height(layout.getHeight() - 2).build();

        // Initialize the list texture
        if (!lazyInit) {
            this.init();
        }
    }

    public AbstractScrollingListComponent(Layout layout, int slotHeight, List<T> data) {
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
    protected abstract void elementChosen(int index);

    protected int calculateIndex(int mouseX, int mouseY) {
        return ((int) (slider.getRatio() * this.movableWindowHeight) + mouseY - this.elementsLayout.getY()) / this.slotHeight;
    }

    public void setSelectedIndex(int index) {
        if (this.selectedIndex != index) {
            this.selectedIndex = index;
            refreshCachedTexture();
            if (this.selectedIndex > -1) {
                elementChosen(this.selectedIndex);
            }
        }
    }

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
        return selectedIndex > -1 && this.dataList.size() > selectedIndex;
    }

    public void add(T item) {
        this.dataList.add(item);
        refresh();
    }

    public boolean contains(T item) {
        return this.dataList.contains(item);
    }

    protected void init() {
        refresh();
    }

    public void refresh() {
        refreshCachedTexture();
        setSliderButtonHeight();
    }

    public List<T> getList() {
        return Collections.unmodifiableList(this.dataList);
    }

    public int getListSize() {
        return this.dataList.size();
    }

    protected void refreshCachedTexture() {
        this.movableWindowHeight = getContentHeight() - this.elementsLayout.getHeight();
        this.elementsLayout.setWidth(layout.getWidth() - 2 - (this.movableWindowHeight > 0 ? 6 : 0));
        final CachedTexture temp = this.cachedTexture;
        this.cachedTexture = createCachedTexture();

        if (temp != null) {
            temp.delete();
        }
    }

    private CachedTexture createCachedTexture() {
        CachedTexture texture = new CachedTexture(elementsLayout.getWidth(), getContentHeight());
        texture.startDrawTexture();

        renderList();

        texture.endDrawTexture();
        return texture;
    }

    protected int getContentHeight() {
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
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderListBackground();
        renderEdgeShadow();
        if (this.movableWindowHeight > 0) {
            slider.render(mouseX, mouseY, partialTicks);
        }
        if (this.dataList.size() > 0) {
            renderCachedTextureList();
        }
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        MousePressedListener press = (x, y) -> {
            if (this.elementsLayout.isIn(x, y)) {
                this._itemClick = true;
            }
        };
        MouseReleasedListener release = (x, y) -> {
            if (this._itemClick && this.elementsLayout.isIn(x, y)) {
                int tempIndex = calculateIndex(x, y);
                if (tempIndex >= this.dataList.size()) {
                    tempIndex = -1;
                }
                setSelectedIndex(tempIndex);
            }
            this._itemClick = false;
        };
        FocusChangedListener focus = f -> {
            if (this._itemClick && !f) {
                this._itemClick = false;
            }
        };
        listener.on(press, release, focus);
    }

    protected void renderListBackground() {
        RenderUtils.drawGradientRect(layout.getLeft(), layout.getTop(), layout.getRight(), layout.getBottom(), 0xC0101010, 0xD0101010);
    }

    protected void renderEdgeShadow() {
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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
    }

    protected void renderSelected(int x, int y) {
        renderSelected(x, y, x + this.elementsLayout.getWidth(), y + this.slotHeight);
    }

    protected void renderSelected(int x, int y, int right, int bottom) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x808080);
        tessellator.addVertexWithUV(x, bottom, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(right, bottom, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(right, y, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
        tessellator.setColorOpaque_I(0);
        tessellator.addVertexWithUV(x + 1, bottom - 1, 0.0D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(right - 1, bottom - 1, 0.0D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(right - 1, y + 1, 0.0D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(x + 1, y + 1, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    protected void renderList() {
        if (this.selectedIndex > -1 && this.selectedIndex < this.dataList.size()) {
            renderSelected(0, this.selectedIndex * this.slotHeight);
        }

        int i = 0;
        for (T data : this.dataList) {
            this.renderSlot(data, 1, i * this.slotHeight);
            i++;
        }
    }

    protected void renderCachedTextureList() {
        final int y = (int) (slider.getRatio() * this.movableWindowHeight);

        final int showHeight = this.movableWindowHeight > 0 ? this.elementsLayout.getHeight() : this.cachedTexture.getHeight();

        // Render elements
        this.cachedTexture.render(elementsLayout.getX(),
                elementsLayout.getY(),
                0,
                y,
                this.cachedTexture.getWidth(),
                showHeight);
    }
}
