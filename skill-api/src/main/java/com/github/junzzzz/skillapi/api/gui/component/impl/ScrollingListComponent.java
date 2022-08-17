package com.github.junzzzz.skillapi.api.gui.component.impl;

import com.github.junzzzz.skillapi.api.gui.base.Layout;
import com.github.junzzzz.skillapi.api.gui.component.AbstractScrollingListComponent;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

/**
 * @author Jun
 * @date 2020/11/20.
 */
public class ScrollingListComponent<T> extends AbstractScrollingListComponent<T> {
    private final SlotRenderer<T> slotRenderer;
    private ItemClickEvent<T> clickEvent;

    @Builder
    public ScrollingListComponent(Layout layout, int slotHeight, List<T> data, SlotRenderer<T> renderer) {
        super(layout, slotHeight, data, true);
        Objects.requireNonNull(renderer);
        this.slotRenderer = renderer;

        this.init();
    }

    public void setClickEvent(ItemClickEvent<T> clickEvent) {
        this.clickEvent = clickEvent;
    }

    @Override
    protected void renderSlot(T data, int x, int y) {
        slotRenderer.renderSlot(data, x, y);
    }

    @Override
    protected void elementChosen(int index) {
        if (clickEvent != null) {
            clickEvent.click(this.getList().get(index), index);
        }
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

    @FunctionalInterface
    public interface ItemClickEvent<T> {
        /**
         * Called when the list item is clicked
         *
         * @param item  Selected item
         * @param index Selected item index
         */
        void click(T item, int index);
    }
}
