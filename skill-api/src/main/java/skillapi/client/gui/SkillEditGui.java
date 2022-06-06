package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.val;
import lombok.var;
import org.lwjgl.input.Keyboard;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.listener.KeyTypedListener;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.api.gui.component.TextFieldComponent;
import skillapi.api.gui.component.impl.ScrollingListComponent;
import skillapi.client.gui.component.SkillEditFormComponent;
import skillapi.common.Translation;
import skillapi.skill.DynamicSkillBuilder;
import skillapi.skill.SkillEffect;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
public final class SkillEditGui extends BaseGui {
    private final SkillEditProfileGui parent;
    private final int labelWidth = 40;
    private Layout formLayout;

    ScrollingListComponent<SkillEffect> effectList;
    private SkillEditFormComponent form;
    private ButtonComponent saveButton;

    final DynamicSkillBuilder skillBuilder;

    public SkillEditGui(SkillEditProfileGui parent, DynamicSkillBuilder builder) {
        this.parent = parent;
        this.skillBuilder = builder;
    }

    @Override
    protected void init() {
        this.formLayout = new Layout(110, 30, this.width - 125, this.height - 140);
        final Layout listLayout = new Layout(10, 10, 100, this.height - 10 - 30);

        ScrollingListComponent.SlotRenderer<SkillEffect> renderer = (data, x, y) -> {
            drawString(Translation.format(data.getUnlocalizedName()), x + 2, y + 5, 0xFFFFFF);
        };

        this.effectList = new ScrollingListComponent<>(listLayout, 25, skillBuilder.getEffectsWithUniversal(), renderer);
        this.effectList.setClickEvent((item, index) -> {
            this.form.clear();
            this.form.setSkillEffect(item);
            this.form.addParams(skillBuilder.getParams(index - 1));
            this.saveButton.setEnable(false);
        });

        this.form = new SkillEditFormComponent(formLayout, labelWidth);

        addComponent(effectList, form);
        // Button
        addButton(10, this.height - 5 - 20, 100, 20, "$skill.constant.edit", () -> displayGui(new SkillEffectChooseGui(this)));

        final int btnPadding = (this.form.getLayout().getWidth() - 200) / 3;
        this.saveButton = addButton(this.form.getLayout().getLeft() + btnPadding, this.height - 5 - 20, 100, 20,
                "$skill.constant.save", this::clickSave);
        this.saveButton.setEnable(false);

        addButton(this.saveButton.getLayout().getRight() + btnPadding, this.saveButton.getLayout().getY(), 100, 20,
                "$skill.constant.finish", this::clickFinish);

        if (skillBuilder.isEmpty()) {
            // Need to be placed at the end
            displayGui(new SkillEffectChooseGui(this));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground();

        // TODO Cache
        drawCenteredString("$skill.gui.editSkill.base.title.param", this.formLayout.getCenterX(), 10, 0xEBEBEB);
    }

    private void drawTextFieldLabel(TextFieldComponent component, String label) {
        drawString(label, component.getLayout().getX() - labelWidth - 5, component.getLayout().getY() + 5, 0xFFFFFF);
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        KeyTypedListener ktl = (c, key) -> {
            if (key == Keyboard.KEY_BACK || checkNumber(c)) {
                // TODO 校验不完善
                val map = this.form.getFormMap();

                val originList = this.skillBuilder.getParams(getIndex());

                for (var e : originList) {
                    if (!e.getValue().equals(map.get(e.getKey()))) {
                        this.saveButton.setEnable(true);
                        break;
                    }
                }

            } else if (this.form.isFocused()) {
                this.saveButton.setEnable(true);
            }
        };

        listener.on(ktl);
    }

    public int getIndex() {
        return this.effectList.getSelectedIndex() - 1;
    }

    private boolean checkNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private void clickSave() {
        final int index = getIndex();
        if (index == -1) {
            this.form.getForm().forEach(param -> this.skillBuilder.setUniversalParam(param.getKey(), param.getValue()));
        } else {
            this.form.getForm().forEach(param -> this.skillBuilder.setParam(index, param.getKey(), param.getValue()));
        }
        this.saveButton.setEnable(false);
        try {
            parent.saveSkill(this.skillBuilder);
        } catch (Exception e) {
            this.saveButton.setEnable(true);
            // TODO fail tip
        }
    }

    private void clickFinish() {
        displayGui(parent);
    }
}
