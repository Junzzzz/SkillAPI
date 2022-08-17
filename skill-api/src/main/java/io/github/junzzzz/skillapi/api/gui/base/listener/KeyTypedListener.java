package io.github.junzzzz.skillapi.api.gui.base.listener;

import io.github.junzzzz.skillapi.api.gui.base.GenericListener;

/**
 * @author Jun
 * @date 2021/3/20.
 */
@FunctionalInterface
public interface KeyTypedListener extends GenericListener {
    /**
     * Called when the keyboard key is pressed
     *
     * @param eventCharacter Key character
     * @param eventKey       Key ID
     * @see org.lwjgl.input.Keyboard
     */
    void keyTyped(char eventCharacter, int eventKey);
}
