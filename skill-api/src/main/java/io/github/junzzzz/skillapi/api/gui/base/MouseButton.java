package io.github.junzzzz.skillapi.api.gui.base;

/**
 * @author Jun
 * @date 2021/3/10.
 */
public enum MouseButton {
    /**
     * Left mouse button
     */
    LEFT(0),
    /**
     * Right mouse button
     */
    RIGHT(1),
    /**
     * Middle mouse button
     */
    MIDDLE(2),
    /**
     * Mouse side button: down
     */
    SIDE_DOWN(3),
    /**
     * Mouse side button: up
     */
    SIDE_UP(4);

    public final int button;

    MouseButton(int button) {
        this.button = button;
    }
}
