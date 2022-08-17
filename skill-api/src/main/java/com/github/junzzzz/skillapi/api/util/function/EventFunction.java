package com.github.junzzzz.skillapi.api.util.function;

/**
 * @author Jun
 * @date 2021/3/9.
 */
@FunctionalInterface
public interface EventFunction {
    /**
     * The event to be executed
     */
    void apply();
}
