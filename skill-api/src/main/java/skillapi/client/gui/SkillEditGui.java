package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.val;
import lombok.var;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.listener.KeyTypedListener;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.api.gui.component.RecordTextFieldComponent;
import skillapi.api.gui.component.TextFieldComponent;
import skillapi.api.gui.component.impl.ScrollingListComponent;
import skillapi.client.gui.component.SkillEditFormComponent;
import skillapi.skill.DynamicSkillBuilder;
import skillapi.skill.SkillEffect;

import static skillapi.api.gui.component.TextFieldComponent.TextFieldType.POSITIVE_INTEGER;
import static skillapi.api.gui.component.TextFieldComponent.TextFieldType.POSITIVE_NUMBER;

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
    private RecordTextFieldComponent name;
    private RecordTextFieldComponent mana;
    private RecordTextFieldComponent cooldown;
    private RecordTextFieldComponent charge;

    final DynamicSkillBuilder skillBuilder;

    public SkillEditGui(SkillEditProfileGui parent, DynamicSkillBuilder builder) {
        this.parent = parent;
        this.skillBuilder = builder;
    }

    @Override
    protected void init() {
        this.formLayout = new Layout(110, 110, this.width - 125, this.height - 140);
        final Layout listLayout = new Layout(10, 10, 100, this.height - 10 - 30);

        ScrollingListComponent.SlotRenderer<SkillEffect> renderer = (data, x, y) -> {
            drawString(I18n.format(data.getUnlocalizedName()), x + 2, y + 5, 0xFFFFFF);
        };

        this.effectList = new ScrollingListComponent<>(listLayout, 25, skillBuilder.getEffects(), renderer);
        this.effectList.setClickEvent((item, index) -> {
            this.form.clear();
            this.form.setSkillEffect(item);
            this.form.addParams(skillBuilder.getParams(index));
            this.saveButton.setEnable(RecordTextFieldComponent.isChanged(this.name, this.mana, this.cooldown, this.charge));
        });

        final Layout nameLayout = Layout.builder()
                .x(this.formLayout.getX() + labelWidth + 10)
                .y(30)
                .width(this.formLayout.getWidth() / 2 - 10 - labelWidth)
                .height(20).build();
        final Layout manaLayout = new Layout(nameLayout.getRight() + labelWidth + 10, 30, nameLayout.getWidth(), 20);
        final Layout cooldownLayout = new Layout(nameLayout.getLeft(), 55, nameLayout.getWidth(), 20);
        final Layout chargeLayout = new Layout(manaLayout.getLeft(), 55, nameLayout.getWidth(), 20);

        this.name = new RecordTextFieldComponent(nameLayout);
        this.mana = new RecordTextFieldComponent(manaLayout);
        this.cooldown = new RecordTextFieldComponent(cooldownLayout);
        this.charge = new RecordTextFieldComponent(chargeLayout);

        this.cooldown.setType(POSITIVE_NUMBER);
        TextFieldComponent.setType(POSITIVE_INTEGER, this.mana, this.charge);

        this.name.setText(skillBuilder.getName());
        this.mana.setText(String.valueOf(skillBuilder.getMana()));
        this.cooldown.setText(String.format("%.3f", skillBuilder.getCooldown() / 1000D));
        this.charge.setText(String.valueOf(skillBuilder.getCharge()));

        this.form = new SkillEditFormComponent(formLayout, labelWidth);

        addComponent(effectList, this.name, this.mana, this.cooldown, this.charge, form);
        // Button
        addButton(10, this.height - 5 - 20, 100, 20, "$skill.constant.edit", () -> displayGui(new SkillEffectChooseGui(this)));

        final int btnPadding = (this.form.getLayout().getWidth() - 200) / 3;
        this.saveButton = addButton(this.form.getLayout().getLeft() + btnPadding, this.height - 5 - 20, 100, 20,
                "Save", this::clickSave);
        this.saveButton.setEnable(false);

        addButton(this.saveButton.getLayout().getRight() + btnPadding, this.saveButton.getLayout().getY(), 100, 20,
                "Finish", this::clickFinish);

        if (skillBuilder.isEmpty()) {
            // Need to be placed at the end
            displayGui(new SkillEffectChooseGui(this));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground();

        drawCenteredString("基本信息", this.formLayout.getCenterX(), 10, 0xEBEBEB);
        drawCenteredString("技能参数", this.formLayout.getCenterX(), 90, 0xEBEBEB);

        drawTextFieldLabel(this.name, "技能名");
        drawTextFieldLabel(this.mana, "所需魔力");
        drawTextFieldLabel(this.cooldown, "冷却时间");
        drawTextFieldLabel(this.charge, "释放等待");
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

                boolean flag = RecordTextFieldComponent.isChanged(this.name, this.mana, this.cooldown, this.charge);

                if (!flag) {
                    for (var e : originList) {
                        if (!e.getValue().equals(map.get(e.getKey()))) {
                            flag = true;
                            break;
                        }
                    }
                }
                this.saveButton.setEnable(flag);
            } else if (this.form.isFocused()) {
                this.saveButton.setEnable(true);
            }
        };

        listener.on(ktl);
    }

    public int getIndex() {
        return this.effectList.getSelectedIndex();
    }

    private boolean checkNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private void clickSave() {
        this.form.getForm()
                .forEach(param -> this.skillBuilder.setParam(getIndex(), param.getKey(), param.getValue()));
        this.skillBuilder.setName(this.name.getText());
        this.skillBuilder.setMana(Integer.parseInt(this.mana.getText()));
        this.skillBuilder.setCooldown((long) (Double.parseDouble(this.cooldown.getText()) * 1000));
        this.skillBuilder.setCharge(Integer.parseInt(this.charge.getText()));
        this.saveButton.setEnable(false);
        try {
            parent.saveSkill(this.skillBuilder);
        } catch (Exception e) {
            this.saveButton.setEnable(true);
            // TODO fail tip
        }
    }

    private boolean checkEmpty(TextFieldComponent component) {
        if (component.getText().isEmpty()) {
            component.setFocused(true);
            return true;
        }
        return false;
    }

    private boolean checkEmpty(TextFieldComponent... component) {
        for (TextFieldComponent c : component) {
            if (checkEmpty(c)) {
                return true;
            }
        }
        return false;
    }

    private void clickFinish() {
        if (checkEmpty(name, mana, cooldown, charge)) {
            return;
        }
        displayGui(parent);
    }
}
