package skillapi.api.gui.component;

import lombok.Setter;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.listener.*;

import java.math.BigInteger;

import static org.lwjgl.input.Keyboard.*;

/**
 * @author Jun
 */
@Setter
public class TextFieldComponent extends BaseComponent {
    private static BigInteger LONG_MAX_VALUE = new BigInteger(Long.toString(Long.MAX_VALUE));

    private final GuiTextField textField;

    private TextFieldType type = TextFieldType.NORMAL;

    /**
     * Takes effect when the type is {@code NORMAL}
     */
    private KeyTypedFilter filter = null;

    public TextFieldComponent(Layout layout) {
        super(layout);
        this.textField = new GuiTextField(getFontRenderer(), layout.getX(), layout.getY(), layout.getWidth(),
                layout.getHeight());
        this.textField.setCanLoseFocus(true);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.textField.drawTextBox();
        }
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        MousePressedListener press = (x, y) -> this.textField.mouseClicked(x, y, 0);
        FocusChangedListener focus = f -> {
            this.textField.setFocused(f);
            Keyboard.enableRepeatEvents(f);
        };
        KeyTypedListener keyType = (character, key) -> {
            // Functional key
            if (character == 1 || character == 3 || character == 22 || character == 24 ||
                    key == KEY_BACK || key == KEY_HOME || key == KEY_LEFT || key == KEY_RIGHT || key == KEY_END || key == KEY_DELETE) {
                this.textField.textboxKeyTyped(character, key);

                // Call listener
                callParent(ComponentUpdateListener.class, l -> l.onUpdate(this));
            } else {
                if (this.type == TextFieldType.NORMAL) {
                    if (filter != null && filter.filter(character, key, this)) {
                        return;
                    }
                } else {
                    if (this.type.filter.filter(character, key, this)) {
                        return;
                    }
                }
                this.textField.textboxKeyTyped(character, key);

                // Call listener
                callParent(ComponentUpdateListener.class, l -> l.onUpdate(this));
            }
        };
        UpdateScreenListener update = this.textField::updateCursorCounter;

        listener.on(press, focus, update);
        listener.on(keyType, Integer.MIN_VALUE);
    }

    public void setType(TextFieldType type) {
        this.type = type;
    }

    public static void setType(TextFieldType type, TextFieldComponent... components) {
        for (TextFieldComponent component : components) {
            component.setType(type);
        }
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

    public void setFocused(boolean focus) {
        this.textField.setFocused(focus);
    }

    public void setEnabled(boolean enabled) {
        this.textField.setEnabled(enabled);
    }

    public enum TextFieldType {
        NORMAL(null),
        POSITIVE_INTEGER((c, i, t) -> {
            if ('0' <= c && c <= '9') {
                return Long.parseLong(t.getText() + c) > Integer.MAX_VALUE;
            }
            return true;
        }),
        POSITIVE_LONG((c, i, t) -> {
            if ('0' <= c && c <= '9') {
                BigInteger number = new BigInteger(t.getText() + c);

                return number.compareTo(LONG_MAX_VALUE) > 0;
            }
            return true;
        }),
        /**
         * Including decimal
         */
        POSITIVE_NUMBER((c, i, t) -> {
            if ('0' <= c && c <= '9') {
                return false;
            } else if (c == '.') {
                String text = t.getText();
                return text.isEmpty() || text.contains(".");
            }
            return true;
        });

        final KeyTypedFilter filter;

        TextFieldType(KeyTypedFilter filter) {
            this.filter = filter;
        }
    }

    @FunctionalInterface
    public interface KeyTypedFilter {
        /**
         * Called when the text field input new character
         *
         * @param eventCharacter Key character
         * @param eventKey       Key ID
         * @return Intercept when the return value is {@code true}
         * @see org.lwjgl.input.Keyboard
         */
        boolean filter(char eventCharacter, int eventKey, TextFieldComponent textField);
    }
}
