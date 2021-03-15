package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.client.gui.component.SkillEffectListComponent;
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
    protected SkillEffectListComponent effectList;

    protected final List<SkillEffectBuilder> selectedEffects = new ArrayList<>();

    public SkillEditGui(SkillConfigGui parent) {
        this.parent = parent;
    }

    @Override
    protected void init() {
        final Layout listLayout = new Layout(10, 10, 100, this.height - 10 - 30);
        this.effectList = new SkillEffectListComponent(listLayout, 25, selectedEffects);

        addComponent(effectList);
        addButton(10, this.height - 5 - 20, 100, 20, "Edit", () -> displayGui(new SkillEffectChooseGui(this)));

        if (selectedEffects.isEmpty()) {
            displayGui(new SkillEffectChooseGui(this));
        }
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground();
    }
}
