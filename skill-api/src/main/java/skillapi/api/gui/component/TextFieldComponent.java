package skillapi.api.gui.component;

import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.Layout;

/**
 * @author Jun
 * @date 2021/3/15.
 */
public class TextFieldComponent extends BaseComponent {
    private final GuiTextField textField;

    private final boolean canLoseFocus;

    public TextFieldComponent(Layout layout) {
        super(layout);
        this.textField = new GuiTextField(getFontRenderer(), layout.getX(), layout.getY(), layout.getWidth(), layout.getHeight());
        this.textField.setCanLoseFocus(true);
        this.canLoseFocus = true;
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
            System.out.println("focus: " + focus);
            this.textField.setFocused(focus);
            Keyboard.enableRepeatEvents(focus);
        }
    }

    @Override
    protected void keyTyped(char eventCharacter, int eventKey) {
        this.textField.textboxKeyTyped(eventCharacter, eventKey);
    }

    @Override
    protected void updateScreen() {
        this.textField.updateCursorCounter();
    }
}
