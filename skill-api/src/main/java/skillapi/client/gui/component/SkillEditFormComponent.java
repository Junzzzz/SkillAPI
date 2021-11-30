package skillapi.client.gui.component;

import net.minecraft.client.resources.I18n;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.component.FormComponent;
import skillapi.api.gui.component.TextFieldComponent;
import skillapi.skill.AbstractSkillEffect;
import skillapi.skill.SkillEffect;

/**
 * @author Jun
 * @date 2021/3/25.
 */
public final class SkillEditFormComponent extends FormComponent {
    private SkillEffect effect;

    public SkillEditFormComponent(Layout layout, int labelWidth) {
        super(layout, labelWidth);
    }

    public void setSkillEffect(SkillEffect effect) {
        this.effect = effect;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        final int x = this.layout.getLeft() + 5;
        int i = this.layout.getTop() + 5;
        for (ParamTextField unit : params) {
            drawString(((I18nParamTextField) unit).translate, x, i, 0xFFFFFF);
            unit.textField.render(mouseX, mouseY, partialTicks);
            i += 25;
        }
    }

    @Override
    public void addParam(String param, String initialValue) {
        final Layout textfield = Layout.builder()
                .x(this.layout.getLeft() + 10 + labelWidth)
                .y(this.layout.getTop() + this.params.size() * 25)
                .height(20)
                .width(this.layout.getWidth() - 10 - labelWidth)
                .build();
        final TextFieldComponent component = new TextFieldComponent(textfield);
        component.setText(initialValue);
        this.params.add(new I18nParamTextField(param, addComponent(component)));
    }

    private final class I18nParamTextField extends ParamTextField {
        String translate;

        public I18nParamTextField(String param, TextFieldComponent textField) {
            super(param, textField);
            if (effect instanceof AbstractSkillEffect) {
                this.translate = I18n.format(((AbstractSkillEffect) effect).getParamName(param));
            } else {
                this.translate = param;
            }
        }
    }
}
