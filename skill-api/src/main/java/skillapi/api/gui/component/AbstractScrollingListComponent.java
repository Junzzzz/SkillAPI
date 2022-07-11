package skillapi.api.gui.component;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.*;
import skillapi.api.gui.base.listener.MouseReleasedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jun
 * @date 2021/2/24.
 */
public abstract class AbstractScrollingListComponent<T> extends BaseComponent {
    private CachedTexture cachedTexture;
    protected final SliderComponent slider;

    private final Layout elementsLayout;
    private final int slotHeight;
    protected int movableWindowHeight;
    private final ArrayList<T> dataList;

    protected int selectedIndex = -1;

    public AbstractScrollingListComponent(Layout layout, int slotHeight, List<T> data, boolean lazyLoad) {
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
        setSliderButtonHeight();

        // Initialize the list texture
        if (!lazyLoad) {
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

    protected void init() {
        refreshCachedTexture();
    }

    private void refreshCachedTexture() {
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
    public void render(int mouseX, int mouseY, float partialTicks) {
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
    protected void listener(ListenerRegistry listener) {
        MouseReleasedListener release = (x, y) -> {
            if (this.elementsLayout.isIn(x, y)) {
                int tempIndex =
                        ((int) (slider.getRatio() * this.movableWindowHeight) + y - this.elementsLayout.getY()) / this.slotHeight;
                if (tempIndex >= this.dataList.size()) {
                    tempIndex = -1;
                }
                if (this.selectedIndex != tempIndex) {
                    this.selectedIndex = tempIndex;
                    refreshCachedTexture();
                    if (this.selectedIndex > -1) {
                        elementChosen(this.selectedIndex);
                    }
                }
            }
        };

        listener.on(release);
    }

    private void renderListBackground() {
        RenderUtils.drawGradientRect(layout.getLeft(), layout.getTop(), layout.getRight(), layout.getBottom(), 0xC0101010, 0xD0101010);
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
        final int x1 = x + this.elementsLayout.getWidth();
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

    protected void renderList() {
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
