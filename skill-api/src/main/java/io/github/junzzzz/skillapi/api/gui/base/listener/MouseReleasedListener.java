package io.github.junzzzz.skillapi.api.gui.base.listener;

import io.github.junzzzz.skillapi.api.gui.base.LocalListener;

/**
 * @author Jun
 */
@FunctionalInterface
public interface MouseReleasedListener extends LocalListener {
    /**
     * Called when the mouse is released
     *
     * @param x Mouse x axis
     * @param y Mouse y axis
     */
    void onReleased(int x, int y);
}
