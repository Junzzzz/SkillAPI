package com.github.junzzzz.skillapi.api.gui.component;

import com.github.junzzzz.skillapi.api.gui.base.Layout;
import com.github.junzzzz.skillapi.utils.MathUtils;

import java.util.List;

/**
 * @author Jun
 */
public abstract class AbstractTileScrollingListComponent<T> extends AbstractScrollingListComponent<T> {
    protected final int slotWidth;
    protected final int colNum;
    protected final int rowNum;

    public AbstractTileScrollingListComponent(Layout layout, int slotHeight, int slotWidth, List<T> data) {
        this(layout, slotHeight, slotWidth, data, false);
    }

    public AbstractTileScrollingListComponent(Layout layout, int slotHeight, int slotWidth, List<T> data, boolean lazyInit) {
        super(layout, slotHeight, data, true);
        this.slotWidth = slotWidth;
        this.colNum = Math.max(this.elementsLayout.getWidth() / this.slotWidth, 1);
        this.rowNum = MathUtils.ceilDiv(this.dataList.size(), colNum);

        if (!lazyInit) {
            this.init();
        }
    }

    @Override
    protected int calculateIndex(int mouseX, int mouseY) {
        int col = (mouseX - this.elementsLayout.getX()) / this.slotWidth;
        int row = ((int) (slider.getRatio() * this.movableWindowHeight) + mouseY - this.elementsLayout.getY()) / this.slotHeight;
        return row * this.colNum + col;
    }

    @Override
    protected int getContentHeight() {
        return this.rowNum * this.slotHeight;
    }

    @Override
    protected void renderList() {
        int offsetX = Math.max((this.elementsLayout.getWidth() - colNum * slotWidth) / (colNum + 1), 0);

        if (this.selectedIndex > -1 && this.selectedIndex < this.dataList.size()) {
            int i = this.selectedIndex % colNum;
            int j = this.selectedIndex / colNum;
            renderSelected(i * (slotWidth + offsetX) + offsetX, j * this.slotHeight, (i + 1) * (slotWidth + offsetX), (j + 1) * this.slotHeight);
        }

        int index = 0;
        for (T data : this.dataList) {
            int i = index % colNum;
            int j = index / colNum;
            this.renderSlot(data, i * (slotWidth + offsetX) + offsetX, j * this.slotHeight);
            index++;
        }
    }
}
