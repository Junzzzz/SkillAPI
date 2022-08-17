package com.github.junzzzz.skillapi.client.gui.component;

import com.github.junzzzz.skillapi.api.gui.base.Layout;
import com.github.junzzzz.skillapi.api.gui.component.AbstractScrollingListComponent;
import com.github.junzzzz.skillapi.api.gui.component.ButtonComponent;
import com.github.junzzzz.skillapi.common.Translation;

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
        this.getFontRenderer().drawString(Translation.format(data), x + 2, y + 2, 0xFFFFFF);
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
