package skillapi.api.gui.base;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jun
 * @date 2020/11/18.
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

    /**
     * Called when the mouse button is pressed
     *
     * @param mouseX Mouse x axis
     * @param mouseY Mouse y axis
     */
    protected abstract void mousePressed(int mouseX, int mouseY);

    /**
     * Called when the mouse button is released
     *
     * @param mouseX Mouse x axis
     * @param mouseY Mouse y axis
     */
    protected abstract void mouseReleased(int mouseX, int mouseY);

    /**
     * Called when the component focus changes
     *
     * @param focus Whether to get focus
     */
    protected void focusChanged(boolean focus) {
        // Overwrite the method when the component needs it
    }

    protected void keyTyped(char eventCharacter, int eventKey) {
        // Overwrite the method when the component needs it
    }

    protected void updateScreen() {
        // Overwrite the method when the component needs it
    }
}
