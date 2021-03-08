package skillapi.client.gui.component;

import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.component.AbstractScrollingListComponent;
import skillapi.skill.BaseSkillEffect;

import java.util.List;

/**
 * @author Jun
 * @date 2021/2/24.
 */
public class SkillEffectListComponent extends AbstractScrollingListComponent<Class<? extends BaseSkillEffect>> {
    private BaseGui.Button associatedButton;

    public SkillEffectListComponent(Layout layout, int slotHeight, List<Class<? extends BaseSkillEffect>> data) {
        super(layout, slotHeight, data);
    }

    @Override
    protected void renderSlot(Class<? extends BaseSkillEffect> data, int x, int y) {
        this.getFontRenderer().drawString(data.getSimpleName(), x, y, 0xFFFFFF);
    }

    @Override
    protected void elementClicked(int index) {
        if (this.associatedButton != null) {
            this.associatedButton.setEnabled(index != -1);
        }
    }

    public void setAssociatedButton(BaseGui.Button button) {
        this.associatedButton = button;
    }

    public List<Class<? extends BaseSkillEffect>> getEffects() {
        return getList();
    }
}
