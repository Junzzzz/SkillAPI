package io.github.junzzzz.skillapi.api.gui.base.listener;

import io.github.junzzzz.skillapi.api.gui.base.GenericListener;

/**
 * @author Jun
 */
@FunctionalInterface
public interface UpdateScreenListener extends GenericListener {
    /**
     * Called from the main game loop to update the screen.
     */
    void onUpdate();
}
