package io.github.junzzzz.skillapi.api.util.function;

/**
 * @author Jun
 */
@FunctionalInterface
public interface EventFunction {
    /**
     * The event to be executed
     */
    void apply();
}
