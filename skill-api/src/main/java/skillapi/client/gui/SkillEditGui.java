package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.component.SliderComponent;
import skillapi.skill.BaseSkillEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jun
 * @date 2020/10/24.
 */
@SideOnly(Side.CLIENT)
public final class SkillEditGui extends BaseGui {
    private final SkillConfigGui parent;
    protected SkillEffectListGui effectListGui;

    protected final List<BaseSkillEffect> selectedEffects = new ArrayList<>();

    public SkillEditGui(SkillConfigGui parent) {
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.effectListGui = new SkillEffectListGui(this, new Layout(10, 10, 100, height - 10 - 30),
                selectedEffects.stream()
                        .map(BaseSkillEffect::getClass)
                        .collect(Collectors.toList())
        );

        final SliderComponent slider = SliderComponent.builder()
                .sliderBox(Layout.builder().x(this.width / 2).y(0).width(20).height(this.height).build()).build();
        // TODO TEST
        slider.setButtonHeight(this.height / 2);
        addComponent(slider);

        addButton(10, height - 5 - 20, 100, 20, "Edit", () ->
                displayGui(new SkillEffectChooseGui(this)));

        if (selectedEffects.isEmpty()) {
            displayGui(new SkillEffectChooseGui(this));
        }
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(0);

        this.effectListGui.drawScreen(mouseX, mouseY, partialTicks);
    }
}
