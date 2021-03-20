package skillapi.api.gui.base.listener;

import skillapi.api.gui.base.BaseListener;

/**
 * @author Jun
 * @date 2021/3/20.
 */
@FunctionalInterface
public interface UpdateScreenListener extends BaseListener {
    /**
     * Called from the main game loop to update the screen.
     */
    void onUpdate();
}
