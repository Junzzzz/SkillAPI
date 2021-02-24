package skillapi.client.gui.component;

import skillapi.api.gui.base.GuiBox;
import skillapi.api.gui.component.AbstractScrollingList;
import skillapi.skill.BaseSkillEffect;

import java.util.List;

/**
 * @author Jun
 * @date 2021/2/24.
 */
public class SkillEffectListComponent extends AbstractScrollingList<Class<? extends BaseSkillEffect>> {
    public SkillEffectListComponent(GuiBox guiBox, int slotHeight, List<Class<? extends BaseSkillEffect>> data) {
        super(guiBox, slotHeight, data);
    }

    @Override
    protected void renderSlot(Class<? extends BaseSkillEffect> data, int x, int y) {

    }
}
