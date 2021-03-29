package skillapi.api.gui.component;

import lombok.AllArgsConstructor;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO SLIDER
 *
 * @author Jun
 * @date 2021/3/20.
 */
public class FormComponent extends BaseComponent {
    protected final int labelWidth;
    protected final List<ParamTextField> params = new LinkedList<>();

    public FormComponent(Layout layout, int labelWidth) {
        super(layout);
        this.labelWidth = labelWidth;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        final int x = this.layout.getLeft() + 5;
        int i = this.layout.getTop() + 5;
        for (ParamTextField unit : params) {
            drawString(unit.param, x, i, 0xFFFFFF);
            unit.textField.render(mouseX, mouseY, partialTicks);
            i += 25;
        }
    }

    @Override
    protected void listener(ListenerRegistry listener) {

    }

    public boolean isFocused() {
        for (ParamTextField param : this.params) {
            if (param.textField.isFocused()) {
                return true;
            }
        }
        return false;
    }

    public void addParam(String param, String initialValue) {
        final Layout textfield = Layout.builder()
                .x(this.layout.getLeft() + 10 + labelWidth)
                .y(this.layout.getTop() + this.params.size() * 25)
                .height(20)
                .width(this.layout.getWidth() - 10 - labelWidth - 5)
                .build();
        final TextFieldComponent component = new TextFieldComponent(textfield);
        component.setText(initialValue);
        this.params.add(new ParamTextField(param, addComponent(component)));
    }

    public void addParams(List<Pair<String, String>> params) {
        for (Pair<String, String> param : params) {
            addParam(param.getKey(), param.getValue());
        }
    }

    public void clear() {
        removeComponents(this.params.stream().map(p -> p.textField).collect(Collectors.toList()));
        this.params.clear();
    }

    public List<Pair<String, String>> getForm() {
        return this.params.stream().map(e -> new Pair<>(e.param, e.textField.getText())).collect(Collectors.toList());
    }

    public Map<String, String> getFormMap() {
        return this.params.stream().collect(Collectors.toMap(k -> k.param, k -> k.textField.getText()));
    }

    @AllArgsConstructor
    protected class ParamTextField {
        public String param;
        public TextFieldComponent textField;
    }
}
