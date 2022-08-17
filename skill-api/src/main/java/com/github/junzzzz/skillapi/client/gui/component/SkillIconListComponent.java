package com.github.junzzzz.skillapi.client.gui.component;

import com.github.junzzzz.skillapi.api.gui.base.Layout;
import com.github.junzzzz.skillapi.api.gui.base.RenderUtils;
import com.github.junzzzz.skillapi.api.gui.component.AbstractTileScrollingListComponent;
import com.github.junzzzz.skillapi.api.gui.component.ButtonComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * @author Jun
 */
public class SkillIconListComponent extends AbstractTileScrollingListComponent<ResourceLocation> {
    private ButtonComponent associatedButton;

    public SkillIconListComponent(Layout layout, List<ResourceLocation> data, ResourceLocation chosen) {
        super(layout, 40, 40, data, true);

        // Set selected
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals(chosen)) {
                this.selectedIndex = i;
                break;
            }
        }

        this.init();
    }

    @Override
    protected void renderSlot(ResourceLocation data, int x, int y) {
        if (data != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            RenderUtils.bindTexture(data);
            RenderUtils.drawTexturedModalRect(x + 4, y + 4, 0, 0, 32, 32, 16, 16, 0.0625D);
        }
    }

    @Override
    protected void elementChosen(int index) {
        if (this.associatedButton != null) {
            this.associatedButton.setEnable(index >= 0);
        }
    }

    public void setAssociatedButton(ButtonComponent button) {
        this.associatedButton = button;
    }

}
