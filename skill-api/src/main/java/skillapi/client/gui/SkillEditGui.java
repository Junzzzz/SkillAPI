package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.listener.KeyTypedListener;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.api.gui.component.FormComponent;
import skillapi.api.gui.component.impl.ScrollingListComponent;
import skillapi.skill.SkillEffectBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * @date 2020/10/24.
 */
@SideOnly(Side.CLIENT)
public final class SkillEditGui extends BaseGui {
    private final SkillConfigGui parent;

    private Layout formLayout;

    protected ScrollingListComponent<SkillEffectBuilder> effectList;
    private FormComponent form;
    private ButtonComponent saveButton;

    private int effectIndex;

    protected final List<SkillEffectBuilder> selectedEffects = new ArrayList<>();

    public SkillEditGui(SkillConfigGui parent) {
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.formLayout = new Layout(110, 10, this.width - 136, this.height - 40);

        final Layout listLayout = new Layout(10, 10, 100, this.height - 10 - 30);
        ScrollingListComponent.SlotRenderer<SkillEffectBuilder> renderer = (data, x, y) -> {
            drawString(data.getName(), x, y, 0xFFFFFF);
        };

        this.effectList = new ScrollingListComponent<>(listLayout, 25, selectedEffects, renderer);
        this.effectList.setClickEvent((item, index) -> {
            this.form.clear();
            this.effectIndex = index;
            this.form.addParams(item.getParamList());
            this.saveButton.setEnable(false);
        });

        this.form = new FormComponent(formLayout, 50);

        addComponent(effectList);
        addComponent(form);
        addButton(10, this.height - 5 - 20, 100, 20, "Edit", () -> displayGui(new SkillEffectChooseGui(this)));
        this.saveButton = addButton(110, this.height - 5 - 20, 100, 20, "Save", () -> {
            final SkillEffectBuilder seb = this.selectedEffects.get(this.effectIndex);
            this.form.getForm().forEach(param -> seb.setParam(param.getParam(), param.getValue()));
            this.saveButton.setEnable(false);
        });
        if (selectedEffects.isEmpty()) {
            displayGui(new SkillEffectChooseGui(this));
        }
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground();
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        KeyTypedListener ktl = (c, key) -> {
            if (key == Keyboard.KEY_BACK) {

            } else {

            }
        };
    }

}
