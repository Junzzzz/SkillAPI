package skillapi.client.gui.component;

import skillapi.api.gui.base.Layout;
import skillapi.api.gui.component.FormComponent;
import skillapi.api.gui.component.TextFieldComponent;
import skillapi.common.Translation;
import skillapi.skill.AbstractSkillEffect;
import skillapi.skill.SkillEffect;
import skillapi.skill.UniversalParam;

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
            drawString(((CacheParamTextField) unit).translation, x, i, 0xFFFFFF);
            unit.textField.render(mouseX, mouseY, partialTicks);
            i += 25;
        }
    }

    @Override
    public void addParam(String param, String initialValue) {
        String translation = effect instanceof AbstractSkillEffect ?
                Translation.format(((AbstractSkillEffect) effect).getParamName(param)) : param;
        translation = effect instanceof UniversalParam ? Translation.format(translation) : translation;

        int width = Math.min((int) (this.layout.getWidth() / 2.5), getFontRenderer().getStringWidth(translation));

        Layout layout = Layout.builder()
                .x(this.layout.getLeft() + 10 + width)
                .y(this.layout.getTop() + this.params.size() * 25)
                .height(20)
                .width(this.layout.getWidth() - 10 - width)
                .build();
        final TextFieldComponent component = new TextFieldComponent(layout);
        component.setText(initialValue);


        this.params.add(new CacheParamTextField(param, translation, addComponent(component)));
    }

    private static final class CacheParamTextField extends ParamTextField {
        final String translation;

        public CacheParamTextField(String param, String translation, TextFieldComponent textField) {
            super(param, textField);
            this.translation = translation;
        }
    }
}
