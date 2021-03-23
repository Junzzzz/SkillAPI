package skillapi.api.gui.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO SLIDER
 *
 * @author Jun
 * @date 2021/3/20.
 */
public class FormComponent extends BaseComponent {
    private final int labelWidth;
    private final List<ParamTextField> params = new LinkedList<>();

    protected FormComponent(Layout layout, int labelWidth) {
        super(layout);
        this.labelWidth = labelWidth;
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
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

    public void addParam(String param) {
        final Layout textfield = Layout.builder()
                .x(this.layout.getLeft() + 10 + labelWidth)
                .y(this.layout.getTop() + this.params.size() * 25)
                .height(20)
                .width(this.layout.getWidth() - 10 - labelWidth - 5)
                .build();
        this.params.add(new ParamTextField(param, new TextFieldComponent(textfield)));
    }

    public List<Param> getForm() {
        return this.params.stream().map(e -> new Param(e.param, e.textField.getText())).collect(Collectors.toList());
    }

    @AllArgsConstructor
    private static class ParamTextField {
        String param;
        TextFieldComponent textField;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Param {
        private String param;
        private String value;
    }
}
