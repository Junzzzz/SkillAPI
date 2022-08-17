package io.github.junzzzz.skillapi.api.gui.base.listener;

import io.github.junzzzz.skillapi.api.gui.base.GenericListener;

/**
 * @author Jun
 * @date 2021/3/20.
 */
@FunctionalInterface
public interface FocusChangedListener extends GenericListener {
    /**
     * Called when the focus is changed
     *
     * @param focus {@code true} when the component has focus, otherwise {@code false}
     */
    void onFocus(boolean focus);
}
