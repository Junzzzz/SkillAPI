package skillapi.api.gui.base.listener;

import skillapi.api.gui.base.BaseListener;

/**
 * @author Jun
 * @date 2021/3/20.
 */
@FunctionalInterface
public interface KeyTypedListener extends BaseListener {
    /**
     * Called when the keyboard key is pressed
     *
     * @param eventCharacter Key character
     * @param eventKey       Key ID
     * @see org.lwjgl.input.Keyboard
     */
    void keyTyped(char eventCharacter, int eventKey);
}
