package com.github.junzzzz.skillapi.api.gui.component;

import com.github.junzzzz.skillapi.api.gui.base.Layout;

/**
 * @author Jun
 */
public class RecordTextFieldComponent extends TextFieldComponent {
    private String originText;

    public RecordTextFieldComponent(Layout layout) {
        super(layout);
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    @Override
    public void setText(String text) {
        if (originText == null) {
            setOriginText(text);
        }
        super.setText(text);
    }

    public boolean isChanged() {
        return !this.getText().equals(originText);
    }

    public static boolean isChanged(RecordTextFieldComponent... components) {
        for (RecordTextFieldComponent component : components) {
            if (component.isChanged()) {
                return true;
            }
        }
        return false;
    }
}
