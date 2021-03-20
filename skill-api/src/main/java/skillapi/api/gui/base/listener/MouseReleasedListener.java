package skillapi.api.gui.base.listener;

import skillapi.api.gui.base.BaseListener;

/**
 * @author Jun
 * @date 2021/3/20.
 */
@FunctionalInterface
public interface MouseReleasedListener extends BaseListener {
    /**
     * Called when the mouse is released
     *
     * @param x Mouse x axis
     * @param y Mouse y axis
     */
    void onReleased(int x, int y);
}
