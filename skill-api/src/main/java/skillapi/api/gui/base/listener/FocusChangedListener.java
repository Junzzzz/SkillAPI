package skillapi.api.gui.base.listener;

import skillapi.api.gui.base.BaseListener;

/**
 * @author Jun
 * @date 2021/3/20.
 */
@FunctionalInterface
public interface FocusChangedListener extends BaseListener {
    /**
     * Called when the focus is changed
     *
     * @param focus {@code true} when the component has focus, otherwise {@code false}
     */
    void onFocus(boolean focus);
}
