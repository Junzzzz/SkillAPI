package com.github.junzzzz.skillapi.api.gui.component;

import com.github.junzzzz.skillapi.api.gui.base.BaseGui;
import com.github.junzzzz.skillapi.api.gui.base.GuiApi;
import com.github.junzzzz.skillapi.api.gui.base.Layout;
import com.github.junzzzz.skillapi.api.gui.base.ListenerRegistry;
import com.github.junzzzz.skillapi.common.Translation;

import java.util.function.Consumer;

/**
 * @author Jun
 */
public class InputDialogGui extends BaseGui {
    private final BaseGui parent;
    private final String title;
    private final String textFieldLabel;
    private final Consumer<String> callback;

    private TextFieldComponent textField;

    public InputDialogGui(BaseGui parent, String title, Consumer<String> callback) {
        this(parent, title, Translation.format("skill.gui.inputDialog.label"), callback);
    }

    public InputDialogGui(BaseGui parent, String title, String label, Consumer<String> callback) {
        this.parent = parent;
        this.callback = callback;
        this.title = title;
        this.textFieldLabel = label;
    }

    @Override
    protected void init() {
        textField = new TextFieldComponent(new Layout(this.width / 2 - 155, 90, 310, 20));
        addButton(this.width / 2 - 155, this.height / 6 + 96, 150, 20, "$gui.done", () -> {
            callback.accept(textField.getText());
            GuiApi.displayGui(parent);
        });
        addButton(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, "$gui.cancel", () -> {
            GuiApi.displayGui(parent);
        });

        addComponent(textField);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(this.title, this.width / 2, 20, 0xFFFFFF);
        drawString(this.textFieldLabel, this.width / 2 - 155, 70, 0xA0A0A0);
    }

    @Override
    protected void listener(ListenerRegistry listener) {

    }

}
