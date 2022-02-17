package skillapi.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.*;
import skillapi.api.gui.base.listener.FocusChangedListener;
import skillapi.api.gui.base.listener.MousePressedListener;
import skillapi.api.gui.base.listener.MouseReleasedListener;
import skillapi.client.GuiKnownSkills;
import skillapi.client.SkillClient;
import skillapi.client.gui.component.KnownSkillListComponent;
import skillapi.packet.OpenSkillProfilesGuiPacket;
import skillapi.packet.SkillBarSyncPacket;
import skillapi.packet.base.Packet;
import skillapi.skill.AbstractSkill;
import skillapi.skill.PlayerSkills;
import skillapi.utils.ClientUtils;

import java.util.ArrayList;

import static skillapi.skill.PlayerSkills.MAX_SKILL_BAR;

/**
 * @author Jun
 */
public class KnownSkillsGui extends BaseGui {
    private final String[] skillKeys = new String[MAX_SKILL_BAR];
    private KnownSkillListComponent skillListComponent;
    private PlayerSkills properties;

    private boolean isDragging = false;
    private int draggingOffsetX;
    private int draggingOffsetY;
    private AbstractSkill draggingSkill;

    @Override
    protected void init() {
        for (int i = 0; i < skillKeys.length; i++) {
            skillKeys[i] = Keyboard.getKeyName(SkillClient.unleashSkillKey[i].getKeyCode());
        }
        this.properties = PlayerSkills.get(ClientUtils.getPlayer());
        Layout listLayout = new Layout(width / 2 - 75, height / 2 - 49, 154, 108);
        Layout sliderLayout = new Layout(width / 2 + 83, height / 2 - 49, 12, 108);

        skillListComponent = new KnownSkillListComponent(listLayout, sliderLayout, new ArrayList<>(properties.getKnownSkills()));
        addComponent(skillListComponent);

        addButton((width - 206) / 2 - 20, (height - 134) / 2, 20, 20, "</>",
                () -> Packet.callback(new OpenSkillProfilesGuiPacket(), profiles -> {
                    GuiApi.displayGui(new SkillProfilesGui(profiles));
                }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawBackground();
        renderSkillBar();
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
                        break;
                    }
                }
                // Sync
                Packet.sendToServer(new SkillBarSyncPacket(properties.getSkillBar()));
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

    private void renderSkillBar() {
        AbstractSkill[] skillBar = properties.getSkillBar();
        for (int i = 0; i < MAX_SKILL_BAR; i++) {
            if (skillBar[i] == null) {
                drawCenteredString(skillKeys[i], width / 2 - 88, height / 2 - 44 + (23 * i), 0xE2E2E9);
            } else {
                String firstName = skillBar[i].getLocalizedName().substring(0, 1);
                drawCenteredString(firstName, width / 2 - 88, height / 2 - 44 + (23 * i), 0xE2E2E9);
            }
        }
    }

    private void renderDragging(int x, int y) {
        if (draggingSkill == null) {
            return;
        }
        String firstStr = draggingSkill.getLocalizedName().substring(0, 1);
        drawCenteredString(firstStr, x + draggingOffsetX, y + draggingOffsetY, 0xFFFFFF);
    }
}
