package skillapi.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.RenderUtils;
import skillapi.api.gui.base.listener.FocusChangedListener;
import skillapi.api.gui.base.listener.MousePressedListener;
import skillapi.api.gui.base.listener.MouseReleasedListener;
import skillapi.client.GuiKnownSkills;
import skillapi.client.SkillAPIClientProxy;
import skillapi.client.gui.component.KnownSkillListComponent;
import skillapi.skill.AbstractSkill;
import skillapi.skill.PlayerSkillProperties;
import skillapi.skill.Skills;
import skillapi.utils.ClientUtils;

/**
 * @author Jun
 */
public class KnownSkillsGui extends BaseGui {
    private final String[] skillKeys = new String[5];
    private KnownSkillListComponent skillListComponent;
    private PlayerSkillProperties properties;

    private boolean isDragging = false;
    private int draggingOffsetX;
    private int draggingOffsetY;
    private AbstractSkill draggingSkill;

    @Override
    protected void init() {
        for (int i = 0; i < skillKeys.length; i++) {
            skillKeys[i] = Keyboard.getKeyName(SkillAPIClientProxy.skillKeyBindings[i].getKeyCode());
        }
        this.properties = PlayerSkillProperties.get(ClientUtils.getPlayer());
        Layout listLayout = new Layout(width / 2 - 75, height / 2 - 49, 154, 108);
        Layout sliderLayout = new Layout(width / 2 + 83, height / 2 - 49, 12, 108);

        skillListComponent = new KnownSkillListComponent(listLayout, sliderLayout, Skills.getAll());
        addComponent(skillListComponent);

        addButton((width - 206) / 2 - 20, (height - 134) / 2, 20, 20, "</>",
                () -> displayGui(new SkillConfigGui()));
        addButton((width - 206) / 2 - 20, (height - 134) / 2 + 25, 20, 20, "<?>", () -> displayGui(new TestGui()));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawBackground();

        // TODO Render skill bar

        // Dragging
        if (isDragging) {
            renderDragging(mouseX, mouseY);
        }
    }

    @Override
    public void drawBackground() {
        GL11.glEnable(GL11.GL_BLEND);
        getTextureManager().bindTexture(GuiKnownSkills.GUI);
        // Frame
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderUtils.drawTexturedModalRect((width - 206) / 2, (height - 134) / 2, 0, 0, 206, 134);

        // Title
        FontRenderer fontRenderer = getFontRenderer();
        fontRenderer.drawStringWithShadow(I18n.format("skill.gui.title"), width / 2 - 73, height / 2 - 61, 0xFCFC80);

        for (int i = 0; i < skillKeys.length; i++) {
            drawCenteredString(skillKeys[i], width / 2 - 88, height / 2 - 44 + (23 * i), 0xE2E2E9);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        MousePressedListener pressed = (x, y) -> {
            if (!isDragging) {
                int index = this.skillListComponent.getMouseOverIndex(x, y);
                if (index != -1) {
                    isDragging = true;
                    draggingOffsetX = this.skillListComponent.getOffsetX(x);
                    draggingOffsetY = this.skillListComponent.getOffsetY(index, y);
                    draggingSkill = this.skillListComponent.getSkill(index);
                }
            }
        };
        Layout[] layouts = new Layout[5];
        for (int i = 0; i < layouts.length; i++) {
            layouts[i] = new Layout(width / 2 - 96, height / 2 - 49 + (23 * i), 16, 16);
        }
        MouseReleasedListener release = (x, y) -> {
            if (isDragging) {
                isDragging = false;
                for (int i = 0; i < layouts.length; i++) {
                    if (layouts[i].isIn(x, y)) {
                        // Set skill bar
                        properties.setSkillBar(i, draggingSkill);
                    }
                }
            }
        };
        FocusChangedListener focus = f -> {
            if (!f) {
                if (isDragging) {
                    isDragging = false;
                }
            }
        };
        listener.on(pressed, release, focus);
    }

    private void renderDragging(int x, int y) {
        if (draggingSkill == null) {
            return;
        }
        String firstStr = draggingSkill.getLocalizedName().substring(0, 1);
        drawCenteredString(firstStr, x + draggingOffsetX, y + draggingOffsetY, 0xFFFFFF);
    }
}
