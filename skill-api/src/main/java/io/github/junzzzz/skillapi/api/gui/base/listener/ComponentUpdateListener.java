package io.github.junzzzz.skillapi.api.gui.base.listener;

import io.github.junzzzz.skillapi.api.gui.base.BaseComponent;
import io.github.junzzzz.skillapi.api.gui.base.LocalListener;

/**
 * @author Jun
 */
@FunctionalInterface
public interface ComponentUpdateListener extends LocalListener {
    /**
     * Called when the component is updated
     *
     * @param component Updated components
     */
    void onUpdate(BaseComponent component);
}
