package io.github.junzzzz.skillapi.api.gui.base.listener;

import io.github.junzzzz.skillapi.api.gui.base.LocalListener;

/**
 * @author Jun
 * @date 2021/3/20.
 */
@FunctionalInterface
public interface MousePressedListener extends LocalListener {
    /**
     * Called when the mouse is pressed
     *
     * @param x Mouse x axis
     * @param y Mouse y axis
     */
    void onPressed(int x, int y);
}
