package skillapi.api.gui.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import javafx.util.Pair;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Jun
 * @date 2021/3/20.
 */
@SideOnly(Side.CLIENT)
public final class ListenerRegistry {
    private final Map<Class<? extends BaseListener>, List<BaseListener>> listenerMap = new ConcurrentHashMap<>(8);

    @SuppressWarnings("unchecked")
    public void on(BaseListener listener) {
        final Class<?>[] interfaces = listener.getClass().getInterfaces();
        if (interfaces.length == 1) {
            listenerMap.computeIfAbsent((Class<? extends BaseListener>) interfaces[0], k -> Collections.synchronizedList(new LinkedList<>())).add(listener);
        }
    }

    public void on(BaseListener... listener) {
        for (BaseListener l : listener) {
            on(l);
        }
    }

    protected void clear() {
        this.listenerMap.clear();
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getListeners(Class<T> clz) {
        return (List<T>) this.listenerMap.get(clz);
    }

    protected <T> void call(Class<T> clz, Caller<T> caller) {
        final List<T> listeners = getListeners(clz);
        if (listeners != null) {
            for (T listener : listeners) {
                caller.call(listener);
            }
        }
    }

    @FunctionalInterface
    protected interface Caller<T> {
        /**
         * Caller
         *
         * @param listener Listener
         */
        void call(T listener);
    }
}
