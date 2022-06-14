package skillapi.api.gui.base.listener;

import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.LocalListener;

/**
 * @author Jun
 */
@FunctionalInterface
public interface ComponentUpdateListener extends LocalListener {
    /**
     * Called when the component is updated
     */
    void onUpdate(BaseComponent component);
}
