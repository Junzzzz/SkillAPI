package skillapi.client.gui.component;

import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.RenderUtils;
import skillapi.api.gui.component.FormComponent;
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
    public void addParam(String param, String initialValue) {
        addParam(param, initialValue, initialValue.equals("false") || initialValue.equals("true"));
    }

    @Override
    public void addParam(String param, String initialValue, boolean bool) {
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
        ParamField paramTextField = bool ? new ParamBooleanField(param, layout)
                : new CacheParamTextField(param, translation, initialValue, layout);
        addComponent(paramTextField.getComponent());
        this.params.add(paramTextField);
    }

    private static final class CacheParamTextField extends ParamTextField {
        final String translation;

        @Override
        public void renderParam(int x, int y) {
            RenderUtils.drawString(this.translation, x, y, 0xFFFFFF);
        }

        public CacheParamTextField(String param, String translation, String initialValue, Layout layout) {
            super(param, initialValue, layout);
            this.translation = translation;
        }
    }
}
