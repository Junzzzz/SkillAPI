package skillapi.client;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.RenderUtils;
import skillapi.client.gui.KnownSkillsGui;
import skillapi.common.Point;
import skillapi.skill.AbstractSkill;
import skillapi.skill.Cooldown;

import java.util.ArrayList;
import java.util.List;

import static skillapi.client.SkillClient.SKILL;

/**
 * @author Jun
 */
public class SkillHud {
    public static void drawHud(int width, int height) {
        if (SKILL == null || !SkillClient.drawHud) {
            // After initialization
            return;
        }
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        drawManaBar(width, height);
        drawSkillBar(width, height);
        drawCooldown(width, height);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static void drawManaBar(int width, int height) {
        int left = width / 2 + 82;
        int y = height - GuiIngameForge.right_height;
        GuiIngameForge.right_height += 10;
        int mana = SKILL.getMana();
        RenderUtils.bindTexture(KnownSkillsGui.GUI);
        for (int i = 0; i < 10; i++) {
            int idx = i * 2 + 1;
            int x = left - i * 8;
            // Background
            RenderUtils.drawTexturedModalRect(x, y, 104, 143, 9, 9);
            if (idx < mana) {
                // Full
                RenderUtils.drawTexturedModalRect(x, y, 113, 143, 9, 9);
            } else if (idx == mana) {
                // Half
                RenderUtils.drawTexturedModalRect(x, y, 122, 143, 9, 9);
            }
        }
    }

    private static void drawSkillBar(int width, int height) {
        int x = width - 22;
        int y = height / 2 - 51;
        RenderUtils.drawTexturedModalRect(x, y, 218, 0, 22, 102);
        Cooldown[] cooldowns = SKILL.getCooldowns();
        AbstractSkill[] skillBar = SKILL.getSkillBar();
        x += 3 + 8;
        y += 3 + 5;
        for (int i = 0; i < skillBar.length; i++) {
            Cooldown cooldown = cooldowns[i];
            AbstractSkill skill = skillBar[i];
            if (cooldown != null && cooldown.isCooledDown()) {
                RenderUtils.bindTexture(KnownSkillsGui.GUI);
                RenderUtils.drawTexturedModalRect(x - 2 - 8, y - 2 - 5 + (20 * i), 219, 102, 20, 20);
            }
            if (skill == null) {
                continue;
            }
            ResourceLocation icon = skill.getIconResource();
            if (icon != null) {
                RenderUtils.bindTexture(icon);
                RenderUtils.drawTexturedModalRect(x - 8, y - 5 + (20 * i), 0, 0, 16, 16, 0.0625D);
            } else {
                String first = skill.getLocalizedName().substring(0, 1);
                RenderUtils.drawCenteredString(first, x, y + (20 * i), 0xFFFFFF);
            }
        }
        GL11.glColor4f(1, 1, 1, 1);
    }

    private static void drawCooldown(int width, int height) {
        int x = width - 22 + 3;
        int y = height / 2 - 51 + 3;
        Cooldown[] cooldowns = SKILL.getCooldowns();
        for (int i = 0; i < cooldowns.length; i++, y += 20) {
            Cooldown cooldown = cooldowns[i];
            if (cooldown == null) {
                continue;
            }
            if (cooldown.isCooledDown()) {
                continue;
            }

            // Cooling status
            Point[] points = getCooldownPointArray(cooldown.getCooledRate(), x, y);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(GL11.GL_POLYGON);
            tessellator.setColorRGBA(255, 255, 255, 127);
            for (Point point : points) {
                tessellator.addVertex(point.x, point.y, 0.0D);
            }
            tessellator.draw();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private static Point[] getCooldownPointArray(double rate, int x, int y) {
        List<Point> points = new ArrayList<>(8);
        Point[] vertex = new Point[]{new Point(x + 16, y), new Point(x + 16, y + 16), new Point(x, y + 16), new Point(x, y)};
        int i;
        if (rate < 0.125) {
            i = 0;
            points.add(new Point(x + 8 + (tan(rate) * 8), y));
        } else if (rate < 0.375) {
            i = 1;
            points.add(new Point(x + 16, y + 8 - (tan(0.25 - rate) * 8)));
        } else if (rate < 0.625) {
            i = 2;
            points.add(new Point(x + 8 + (tan(0.5 - rate) * 8), y + 16));
        } else if (rate < 0.875) {
            i = 3;
            points.add(new Point(x, y + 8 + (tan(0.75 - rate) * 8)));
        } else {
            i = 4;
            points.add(new Point(x + 8 - (tan(1 - rate) * 8), y));
        }
        for (; i < 4; i++) {
            points.add(vertex[i]);
        }
        points.add(new Point(x + 8, y));
        points.add(new Point(x + 8, y + 8));
        Point[] result = new Point[points.size()];
        for (int j = points.size() - 1; j >= 0; j--) {
            result[points.size() - 1 - j] = points.get(j);
        }
        return result;
    }

    private static float tan(double a) {
        float f = (float) (a * 2 * Math.PI);
        return MathHelper.sin(f) / MathHelper.cos(f);
    }
}
