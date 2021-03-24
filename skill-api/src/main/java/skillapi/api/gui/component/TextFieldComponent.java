package skillapi.api.gui.component;

import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.listener.FocusChangedListener;
import skillapi.api.gui.base.listener.KeyTypedListener;
import skillapi.api.gui.base.listener.MousePressedListener;
import skillapi.api.gui.base.listener.UpdateScreenListener;

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
        if (this.visible) {
            this.textField.drawTextBox();
        }
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        MousePressedListener press = (x, y) -> this.textField.mouseClicked(x, y, 0);
        FocusChangedListener focus = f -> {
            if (this.canLoseFocus) {
                this.textField.setFocused(f);
                Keyboard.enableRepeatEvents(f);
            }
        };
        KeyTypedListener keyType = this.textField::textboxKeyTyped;
        UpdateScreenListener update = this.textField::updateCursorCounter;

        listener.on(press, focus, keyType, update);
    }

    public void setMaxLength(int length) {
        this.textField.setMaxStringLength(length);
    }

    public String getText() {
        return this.textField.getText();
    }

    public void setText(String text) {
        this.textField.setText(text);
    }

    public boolean isFocused() {
        return this.textField.isFocused();
    }

    public void setEnabled(boolean enabled) {
        this.textField.setEnabled(enabled);
    }
}
