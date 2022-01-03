package skillapi.client;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.RenderUtils;
import skillapi.skill.AbstractSkill;

import static skillapi.client.SkillClient.SKILL;

/**
 * @author Jun
 */
public class SkillHud {
    public static void drawHud(int width, int height) {
        if (SKILL == null) {
            // After initialization
            return;
        }
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        drawManaBar(width, height);
        drawSkillBar(width, height);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static void drawManaBar(int width, int height) {
        int left = width / 2 + 82;
        int y = height - GuiIngameForge.right_height;
        GuiIngameForge.right_height += 10;
        int mana = SKILL.getMana();
        RenderUtils.bindTexture(GuiKnownSkills.GUI);
        for (int i = 0; i < 10; i++) {
            int idx = i * 2 + 1;
            int x = left - i * 8;
            // Background
            RenderUtils.drawTexturedModalRect(x, y, 104, 143, 9, 9);
            if (idx < mana) {
                // Full
                RenderUtils.drawTexturedModalRect(x, y, 113, 143, 9, 9);
            } else {
                // Half
                RenderUtils.drawTexturedModalRect(x, y, 122, 143, 9, 9);
            }
        }
    }

    private static void drawSkillBar(int width, int height) {
        int x = width - 22;
        int y = height / 2 - 51;
        RenderUtils.drawTexturedModalRect(x, y, 218, 0, 22, 102);
        AbstractSkill[] skillBar = SKILL.getSkillBar();
        x += 3 + 8;
        y += 3 + 5;
        for (int i = 0; i < skillBar.length; i++) {
            if (skillBar[i] != null) {
                String first = skillBar[i].getLocalizedName().substring(0, 1);
                RenderUtils.drawCenteredString(first, x, y + (20 * i), 0xFFFFFF);
            }
        }
        GL11.glColor4f(1, 1, 1, 1);
    }
}
