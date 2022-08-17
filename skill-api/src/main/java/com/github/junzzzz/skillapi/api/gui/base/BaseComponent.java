package com.github.junzzzz.skillapi.api.gui.base;

import com.github.junzzzz.skillapi.api.gui.base.ListenerRegistry.Caller;
import com.github.junzzzz.skillapi.api.gui.base.ListenerRegistry.ComponentCaller;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jun
 */
public abstract class BaseComponent extends GenericGui {
    @Getter
    protected final Layout layout;

    @Getter
    @Setter
    protected boolean visible;

    protected BaseComponent(Layout layout) {
        this.layout = layout;
        this.visible = true;
    }

    public final <T extends GenericListener> void callParent(Class<T> clz, Caller<T> caller) {
        if (this.parent instanceof BaseComponent) {
            this.parent.parent.listenerRegistry.call(clz, caller, false);
        } else if (this.parent instanceof BaseGui) {
            this.parent.listenerRegistry.call(clz, caller, false);
        }
    }

    public final <T extends GenericListener> void callParent(Class<T> clz, ComponentCaller<T> caller) {
        if (this.parent instanceof BaseComponent) {
            this.parent.parent.listenerRegistry.call(clz, caller, false);
        } else if (this.parent instanceof BaseGui) {
            this.parent.listenerRegistry.call(clz, caller, false);
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
