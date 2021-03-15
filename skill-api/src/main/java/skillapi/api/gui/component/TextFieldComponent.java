package skillapi.api.gui.component;

import net.minecraft.client.gui.GuiTextField;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.Layout;

/**
 * @author Jun
 * @date 2021/3/15.
 */
public class TextFieldComponent extends BaseComponent {
    private final GuiTextField textField;

    private boolean canLoseFocus;

    protected TextFieldComponent(Layout layout) {
        super(layout);
        this.textField = new GuiTextField(getFontRenderer(), layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight());
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        this.textField.drawTextBox();
    }

    @Override
    protected void mousePressed(int mouseX, int mouseY) {
        this.textField.mouseClicked(mouseX, mouseY, 0);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY) {

    }

    @Override
    protected void focusChanged(boolean focus) {
        if (this.canLoseFocus) {
            this.textField.setFocused(focus);
        }
    }
}
