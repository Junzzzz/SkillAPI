package skillapi.api.gui.base;

import net.minecraft.client.gui.FontRenderer;

/**
 * @author Jun
 * @date 2020/11/18.
 */
public abstract class BaseComponent {
    protected final GuiBox guiBox;


    protected BaseComponent(GuiBox guiBox) {
        this.guiBox = guiBox;
    }

    public FontRenderer getFontRenderer() {
        return GuiConst.getFontRenderer();
    }

    public int getScaleFactor() {
        return GuiConst.getScaleFactor();
    }

    /**
     * Render function
     *
     * @param mouseX       Mouse x axis
     * @param mouseY       Mouse y axis
     * @param partialTicks Time tick
     */
    protected abstract void render(int mouseX, int mouseY, float partialTicks);

    /**
     * Called when the mouse button is pressed
     *
     * @param mouseX Mouse x axis
     * @param mouseY Mouse y axis
     * @return Whether to intercept
     */
    protected abstract boolean mousePressed(int mouseX, int mouseY);
    /**
     * Called when the mouse button is released
     *
     * @param mouseX Mouse x axis
     * @param mouseY Mouse y axis
     * @return Whether to intercept
     */
    protected abstract boolean mouseReleased(int mouseX, int mouseY);
}
