package com.github.junzzzz.skillapi.api.gui.component;

import com.github.junzzzz.skillapi.api.gui.base.BaseComponent;
import com.github.junzzzz.skillapi.api.gui.base.Layout;
import com.github.junzzzz.skillapi.api.gui.base.ListenerRegistry;
import com.github.junzzzz.skillapi.api.gui.base.RenderUtils;
import com.github.junzzzz.skillapi.api.gui.base.listener.ComponentUpdateListener;
import com.github.junzzzz.skillapi.api.util.Pair;
import com.github.junzzzz.skillapi.skill.DynamicSkillParam;
import lombok.AllArgsConstructor;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO SLIDER
 *
 * @author Jun
 */
public class FormComponent extends BaseComponent {
    protected final int labelWidth;
    protected final List<ParamField> params = new LinkedList<>();

    private final Set<BaseComponent> formComponents = new HashSet<>(8);

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
        ComponentUpdateListener cul = component -> {
            if (formComponents.contains(component)) {
                callParent(ComponentUpdateListener.class, l -> l.onUpdate(this));
            }
        };

        listener.on(cul);
    }

    public boolean isFocused() {
        for (ParamField param : this.params) {
            if (param instanceof ParamTextField && ((ParamTextField) param).textField.isFocused()) {
                return true;
            }
        }
        return false;
    }

    public void addParam(String paramName, DynamicSkillParam param) {
        Layout textfield = Layout.builder()
                .x(this.layout.getLeft() + 10 + labelWidth)
                .y(this.layout.getTop() + this.params.size() * 25)
                .height(20)
                .width(this.layout.getWidth() - 10 - labelWidth - 5)
                .build();
        Class<?> type = param.getOriginalType();
        ParamField paramField;
        if (type == boolean.class || type == Boolean.class) {
            paramField = new ParamBooleanField(paramName, param.getValue(), layout);
        } else {
            paramField = new ParamTextField(paramName, param.getValue(), layout);
        }
        addFormComponent(paramField.getComponent());
        this.params.add(paramField);
    }

    private void addFormComponent(BaseComponent component) {
        addComponent(component);
        formComponents.add(component);
    }

    public void addParams(List<Map.Entry<String, DynamicSkillParam>> params) {
        for (Map.Entry<String, DynamicSkillParam> param : params) {
            addParam(param.getKey(), param.getValue());
        }
    }

    public void clear() {
        removeComponents(this.params.stream().map(ParamField::getComponent).collect(Collectors.toList()));
        this.params.clear();
        this.formComponents.clear();
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

        public ParamBooleanField(String param, String initialValue, Layout layout) {
            super(param);
            this.button = new ButtonComponent(layout, getText(), this::clickButton);
            setBool(Boolean.parseBoolean(initialValue));
        }

        public void setBool(boolean bool) {
            this.bool = bool;
            this.button.setTextAndColor(getText(), bool ? Color.green.getRGB() : Color.red.getRGB());
        }

        private void clickButton() {
            setBool(!this.bool);
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
