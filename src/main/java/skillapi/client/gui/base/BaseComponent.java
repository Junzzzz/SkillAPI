package skillapi.client.gui.base;

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
}
