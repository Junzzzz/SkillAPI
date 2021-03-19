package skillapi.client.gui.component;

import skillapi.api.gui.base.Layout;
import skillapi.api.gui.component.AbstractScrollingListComponent;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.skill.SkillEffectBuilder;

import java.util.List;

/**
 * @author Jun
 * @date 2021/2/24.
 */
public class SkillEffectListComponent extends AbstractScrollingListComponent<SkillEffectBuilder> {
    private ButtonComponent associatedButton;

    public SkillEffectListComponent(Layout layout, int slotHeight, List<SkillEffectBuilder> data) {
        super(layout, slotHeight, data);
    }

    @Override
    protected void renderSlot(SkillEffectBuilder data, int x, int y) {
        this.getFontRenderer().drawString(data.getName(), x, y, 0xFFFFFF);
    }

    @Override
    protected void elementClicked(int index) {
        if (this.associatedButton != null) {
            this.associatedButton.setEnable(index != -1);
        }
    }

    public void setAssociatedButton(ButtonComponent button) {
        this.associatedButton = button;
    }
}
