package io.github.junzzzz.skillapi.client.gui.component;

import io.github.junzzzz.skillapi.api.gui.base.BaseComponent;
import io.github.junzzzz.skillapi.api.gui.base.Layout;
import io.github.junzzzz.skillapi.api.gui.base.RenderUtils;
import io.github.junzzzz.skillapi.api.gui.component.FormComponent;
import io.github.junzzzz.skillapi.client.gui.SkillIconChooseGui;
import io.github.junzzzz.skillapi.common.Translation;
import io.github.junzzzz.skillapi.skill.*;
import net.minecraft.util.ResourceLocation;

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
    public void addParam(String paramName, DynamicSkillParam param) {
        String translation = effect instanceof AbstractSkillEffect ?
                Translation.format(((AbstractSkillEffect) effect).getParamName(paramName)) : paramName;
        translation = effect instanceof UniversalParam ? Translation.format(translation) : translation;

        int width = Math.min((int) (this.layout.getWidth() / 2.5), getFontRenderer().getStringWidth(translation));

        Layout layout = Layout.builder()
                .x(this.layout.getLeft() + 10 + width)
                .y(this.layout.getTop() + this.params.size() * 25)
                .height(20)
                .width(this.layout.getWidth() - 10 - width)
                .build();

        Class<?> type = param.getOriginalType();

        ParamField paramField;
        if (type == boolean.class || type == Boolean.class) {
            // Boolean field
            paramField = new ParamBooleanField(paramName, param.getValue(), layout);
        } else if (type == ResourceLocation.class) {
            // Skill icon field
            paramField = new IconChooseField(paramName, param.getValue(), layout.getX(), layout.getY());
        } else {
            // Text field
            paramField = new ParamTextField(paramName, param.getValue(), layout);
        }
        paramField = new TranslationFieldProxy(paramField, translation);

        // Save component
        addComponent(paramField.getComponent());
        this.params.add(paramField);
    }

    private static final class TranslationFieldProxy extends ParamField {
        ParamField paramField;
        final String translation;

        public TranslationFieldProxy(ParamField paramField, String translation) {
            super(paramField.getParam());
            this.paramField = paramField;
            this.translation = translation;
        }

        @Override
        public void renderParam(int x, int y) {
            RenderUtils.drawString(this.translation, x, y, 0xFFFFFF);
        }

        @Override
        public BaseComponent getComponent() {
            return paramField.getComponent();
        }

        @Override
        public String getText() {
            return paramField.getText();
        }
    }

    private static final class IconChooseField extends ParamField {
        final SkillIconChooseGui.SkillIconChooseButtonComponent component;

        public IconChooseField(String param, String skillIconStr, int x, int y) {
            super(param);
            this.component = new SkillIconChooseGui.SkillIconChooseButtonComponent(x, y, SkillIcon.valueOf(skillIconStr));
        }

        @Override
        public BaseComponent getComponent() {
            return this.component;
        }

        @Override
        public String getText() {
            return this.component.getIconStr();
        }
    }
}
