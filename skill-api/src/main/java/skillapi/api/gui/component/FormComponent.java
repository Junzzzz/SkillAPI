package skillapi.api.gui.component;

import lombok.AllArgsConstructor;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.RenderUtils;
import skillapi.api.util.Pair;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO SLIDER
 *
 * @author Jun
 */
public class FormComponent extends BaseComponent {
    protected final int labelWidth;
    protected final List<ParamField> params = new LinkedList<>();

    public FormComponent(Layout layout, int labelWidth) {
        super(layout);
        this.labelWidth = labelWidth;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        final int x = this.layout.getLeft() + 5;
        int i = this.layout.getTop() + 5;
        for (ParamField unit : params) {
            unit.renderParam(x, i);
            unit.render(mouseX, mouseY, partialTicks);
            i += 25;
        }
    }

    @Override
    protected void listener(ListenerRegistry listener) {

    }

    public boolean isFocused() {
        for (ParamField param : this.params) {
            if (param instanceof ParamTextField && ((ParamTextField) param).textField.isFocused()) {
                return true;
            }
        }
        return false;
    }

    public void addParam(String param, String initialValue) {
        addParam(param, initialValue, false);
    }

    public void addParam(String param, String initialValue, boolean bool) {
        Layout textfield = Layout.builder()
                .x(this.layout.getLeft() + 10 + labelWidth)
                .y(this.layout.getTop() + this.params.size() * 25)
                .height(20)
                .width(this.layout.getWidth() - 10 - labelWidth - 5)
                .build();
        ParamField paramField = bool ? new ParamBooleanField(param, layout) : new ParamTextField(param, initialValue, layout);
        addComponent(paramField.getComponent());
        this.params.add(paramField);
    }

    public void addParams(List<Map.Entry<String, String>> params) {
        for (Map.Entry<String, String> param : params) {
            addParam(param.getKey(), param.getValue());
        }
    }

    public void clear() {
        removeComponents(this.params.stream().map(ParamField::getComponent).collect(Collectors.toList()));
        this.params.clear();
    }

    public List<Pair<String, String>> getForm() {
        return this.params.stream().map(e -> new Pair<>(e.param, e.getText())).collect(Collectors.toList());
    }

    public Map<String, String> getFormMap() {
        return this.params.stream().collect(Collectors.toMap(k -> k.param, ParamField::getText));
    }

    @AllArgsConstructor
    protected static abstract class ParamField {
        protected String param;

        public void renderParam(int x, int y) {
            RenderUtils.drawString(this.param, x, y, 0xFFFFFF);
        }

        public void render(int mouseX, int mouseY, float partialTicks) {
            getComponent().render(mouseX, mouseY, partialTicks);
        }

        public String getParam() {
            return param;
        }

        public abstract BaseComponent getComponent();

        public abstract String getText();
    }

    protected static class ParamTextField extends ParamField {
        protected TextFieldComponent textField;

        public ParamTextField(String param, String initialValue, Layout layout) {
            super(param);
            this.textField = new TextFieldComponent(layout);
            this.textField.setText(initialValue);
        }

        @Override
        public BaseComponent getComponent() {
            return this.textField;
        }

        @Override
        public String getText() {
            return this.textField.getText();
        }
    }

    protected static class ParamBooleanField extends ParamField {
        protected final ButtonComponent button;

        protected boolean bool;

        public ParamBooleanField(String param, Layout layout) {
            super(param);
            this.bool = false;
            this.button = new ButtonComponent(layout, getText(), this::clickButton);
        }

        private void clickButton() {
            this.bool = !this.bool;

            this.button.setTextAndColor(getText(), bool ? Color.green.getRGB() : Color.red.getRGB());
        }

        @Override
        public BaseComponent getComponent() {
            return this.button;
        }

        @Override
        public String getText() {
            return String.valueOf(this.bool);
        }
    }

}
