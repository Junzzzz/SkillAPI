package com.github.junzzzz.skillapi.api.gui.base;

import com.github.junzzzz.skillapi.common.SkillRuntimeException;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.AllArgsConstructor;
import lombok.var;

import java.util.*;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
public final class ListenerRegistry {
    private static final Comparator<PriorityListener> PRIORITY_COMPARATOR = Comparator.comparingInt(o -> o.priority);

    private static Map<Class<? extends GenericListener>, Map<BaseComponent, List<PriorityListener>>> GLOBAL;

    private final GenericGui base;
    private final Map<Class<? extends GenericListener>, Map<BaseComponent, List<PriorityListener>>> local =
            new HashMap<>(8);

    private BaseComponent mappingComponent;

    public ListenerRegistry(GenericGui ui) {
        this.base = ui;
    }

    void onComponent(BaseComponent mappingComponent) {
        this.mappingComponent = mappingComponent;
    }

    static void init() {
        GLOBAL = new HashMap<>(8);
    }

    static void clean() {
        GLOBAL.clear();
        GLOBAL = null;
    }

    /**
     * Can only be executed in BaseGui
     */
    void clear() {
        GLOBAL.clear();
        this.local.clear();
    }

    public void removeListener(BaseComponent component) {
        for (var value : GLOBAL.values()) {
            value.remove(component);
        }
        for (var value : this.local.values()) {
            value.remove(component);
        }
    }

    public void removeListeners(List<BaseComponent> component) {
        for (var v : GLOBAL.values()) {
            for (BaseComponent c : component) {
                v.remove(c);
            }
        }
        for (var v : this.local.values()) {
            for (BaseComponent c : component) {
                v.remove(c);
            }
        }
    }

    public void on(GenericListener listener) {
        on(listener, 0);
    }

    @SuppressWarnings("unchecked")
    public void on(GenericListener listener, int priority) {
        final Class<?>[] interfaces = listener.getClass().getInterfaces();
        if (interfaces.length != 1) {
            throw new SkillRuntimeException("Unknown error");
        }
        final Class<? extends GenericListener> listenerClass = (Class<? extends GenericListener>) interfaces[0];
        if (listener instanceof LocalListener) {
            local.computeIfAbsent(listenerClass, k -> new LinkedHashMap<>(4))
                    .computeIfAbsent(mappingComponent, k -> new ArrayList<>())
                    .add(new PriorityListener(mappingComponent, listener, priority));

        } else {
            GLOBAL.computeIfAbsent(listenerClass, k -> new LinkedHashMap<>(4))
                    .computeIfAbsent(mappingComponent, k -> new ArrayList<>())
                    .add(new PriorityListener(mappingComponent, listener, priority));
        }
    }

    public void on(GenericListener... listener) {
        for (GenericListener l : listener) {
            on(l);
        }
    }

    public <T extends GenericListener> void call(Class<T> clz, ComponentCaller<T> caller) {
        call(clz, caller, true);
    }

    @SuppressWarnings("unchecked")
    public <T extends GenericListener> void call(Class<T> clz, ComponentCaller<T> caller, boolean recursive) {
        var map = GLOBAL;
        if (LocalListener.class.isAssignableFrom(clz)) {
            if (recursive) {
                for (BaseComponent component : this.base.components) {
                    component.listenerRegistry.call(clz, caller);
                }
            }
            map = this.local;
        }
        if (map == null) {
            return;
        }
        final Map<BaseComponent, List<PriorityListener>> m = map.get(clz);
        if (m == null) {
            return;
        }
        m.entrySet().stream().flatMap(e -> e.getValue().stream()).sorted(PRIORITY_COMPARATOR)
                .forEach(l -> caller.call(l.component, (T) l.listener));
    }

    public <T extends GenericListener> void call(Class<T> clz, Caller<T> caller) {
        call(clz, caller, true);
    }

    @SuppressWarnings("unchecked")
    public <T extends GenericListener> void call(Class<T> clz, Caller<T> caller, boolean recursive) {
        var map = GLOBAL;
        if (LocalListener.class.isAssignableFrom(clz)) {
            if (recursive) {
                for (BaseComponent component : this.base.components) {
                    component.listenerRegistry.call(clz, caller);
                }
            }
            map = this.local;
        }
        if (map == null) {
            return;
        }
        Map<BaseComponent, List<PriorityListener>> m = map.get(clz);
        if (m == null) {
            return;
        }
        m.values().stream().flatMap(Collection::stream)
                .sorted(PRIORITY_COMPARATOR)
                .forEach(p -> caller.call((T) p.listener));
    }

    @SuppressWarnings("unchecked")
    public static <T extends GenericListener> void call(BaseComponent component, Class<T> clz, Caller<T> caller) {
        if (LocalListener.class.isAssignableFrom(clz)) {
            throw new SkillRuntimeException("Not supported.");
        }
        if (GLOBAL == null) {
            return;
        }
        // Global only
        final Map<BaseComponent, List<PriorityListener>> m = GLOBAL.get(clz);
        if (m == null) {
            return;
        }
        final List<PriorityListener> listeners = m.get(component);
        if (listeners == null) {
            return;
        }
        listeners.sort(PRIORITY_COMPARATOR);
        for (PriorityListener pl : listeners) {
            caller.call((T) pl.listener);
        }
    }

    public interface ComponentCaller<T> {
        void call(BaseComponent genericGui, T listener);
    }

    public interface Caller<T> {
        void call(T listener);
    }

    @AllArgsConstructor
    static class PriorityListener {
        BaseComponent component;
        GenericListener listener;
        int priority;
    }
}
