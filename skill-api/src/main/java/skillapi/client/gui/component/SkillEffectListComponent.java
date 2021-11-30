package skillapi.client.gui.component;

import net.minecraft.client.resources.I18n;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.component.AbstractScrollingListComponent;
import skillapi.api.gui.component.ButtonComponent;

import java.util.List;

/**
 * @author Jun
 * @date 2021/2/24.
 */
public final class SkillEffectListComponent extends AbstractScrollingListComponent<String> {
    private ButtonComponent associatedButton;

    public SkillEffectListComponent(Layout layout, int slotHeight, List<String> data) {
        super(layout, slotHeight, data);
    }

    @Override
    protected void renderSlot(String data, int x, int y) {
        this.getFontRenderer().drawString(I18n.format(data), x, y, 0xFFFFFF);
    }

    @Override
    protected void elementChosen(int index) {
        if (this.associatedButton != null) {
            this.associatedButton.setEnable(index != -1);
        }
    }

    public void setAssociatedButton(ButtonComponent button) {
        this.associatedButton = button;
    }
}
