package skillapi.api.gui.component.impl;

import lombok.Builder;
import skillapi.api.gui.base.GuiBox;
import skillapi.api.gui.component.AbstractScrollingList;

import java.util.List;
import java.util.Objects;

/**
 * @author Jun
 * @date 2020/11/20.
 */
public class ScrollingListComponent<T> extends AbstractScrollingList<T> {
    private final SlotRenderer<T> slotRenderer;

    @Builder
    public ScrollingListComponent(GuiBox guiBox, int slotHeight, List<T> data, SlotRenderer<T> renderer) {
        super(guiBox, slotHeight, data, true);
        Objects.requireNonNull(renderer);
        this.slotRenderer = renderer;

        this.init();
    }

    @Override
    protected void renderSlot(T data, int x, int y) {
        slotRenderer.renderSlot(data, x, y);
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
