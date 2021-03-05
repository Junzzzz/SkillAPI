package skillapi.client;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.opengl.GL11;
import skillapi.*;
import skillapi.packets.LearnSkillPacket;
import skillapi.packets.SkillPacket;

import java.util.Iterator;

public final class HudSkills {
    private final Minecraft game;
    public int skillGetTimer = 50;
    private PlayerSkills player = null;

    public HudSkills() {
        this.game = Minecraft.getMinecraft();
    }

    public void drawHUD(int scaledWidth, int scaledHeight, float partialTicks) {
        if (game.thePlayer != null) {
            player = PlayerSkills.get(game.thePlayer);
        }
        if (player != null) {
            GL11.glEnable(3042 /* GL_BLEND */);
            GL11.glBlendFunc(770, 771);
            drawManaBar(scaledWidth, scaledHeight);
            drawSkillBar(scaledWidth, scaledHeight);
            drawChargeup(scaledWidth, scaledHeight);
            drawActiveSkills(scaledWidth, scaledHeight);
            drawSkillGet(scaledWidth, scaledHeight);
            GL11.glDisable(3042 /* GL_BLEND */);
        }
    }

    private void drawManaBar(int scaledWidth, int scaledHeight) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        game.renderEngine.bindTexture(GuiKnownSkills.GUI);
        for (int manaPip = 0; manaPip < 10; manaPip++) {
            int posY = scaledHeight - GuiIngameForge.right_height;
            int posX = (scaledWidth / 2 + 10) + manaPip * 8;
            game.ingameGUI.drawTexturedModalRect(posX, posY, 104, 143, 9, 9); //BG
            if (player.getMana() < player.getPrevMana()) {
                if (manaPip * 2 + 1 < player.getPrevMana()) {
                    game.ingameGUI.drawTexturedModalRect(posX, posY, 131, 143, 9, 9); //DMG Full pip
                }
                if (manaPip * 2 + 1 == player.getPrevMana()) {
                    game.ingameGUI.drawTexturedModalRect(posX, posY, 140, 143, 9, 9); //DMG Half pip
                }
            }
            if (manaPip * 2 + 1 < player.getMana()) {
                game.ingameGUI.drawTexturedModalRect(posX, posY, 113, 143, 9, 9); //Full pip
            }
            if (manaPip * 2 + 1 == player.getMana()) {
                game.ingameGUI.drawTexturedModalRect(posX, posY, 122, 143, 9, 9); //Half pip
            }
        }
        GuiIngameForge.right_height += 10;
    }

    private void drawSkillBar(int scaledWidth, int scaledHeight) {
        boolean isBarEmpty = true;
        for (Skill skill : player.skillBar) {
            if (skill != null) {
                isBarEmpty = false;
                break;
            }
        }
        if (isBarEmpty) {
            return;
        }
        int posX, posY;
        game.ingameGUI.drawTexturedModalRect(posX = (scaledWidth - 22), posY = ((scaledHeight - 102) / 2), 218, 0, 22, 102);
        for (int i = 0; i < player.skillBar.length; i++) {
            if (player.skillBar[i] != null) {
                game.renderEngine.bindTexture(player.skillBar[i].getTexture());
                GL11.glScalef(0.0625F, 0.0625F, 1F);
                if (!player.skillBar[i].canPlayerUseSkill(game.thePlayer)) {
                    GL11.glColor4f(0.4F, 0.4F, 0.4F, 1.0F);
                }
                game.ingameGUI.drawTexturedModalRect((posX + 3) * 16, (posY + 3 + (20 * i)) * 16, 0, 0, 256, 256);
                GL11.glScalef(16F, 16F, 1F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                game.renderEngine.bindTexture(GuiKnownSkills.GUI);
                if (player.skillBar[i].cooldownFrame >= 1) {
                    game.ingameGUI.drawTexturedModalRect(posX + 3, posY + 3 + (20 * i), 240, player.skillBar[i].cooldownFrame * 16, 16, 16);
                }
            }
        }
    }

    private void drawChargeup(int scaledWidth, int scaledHeight) {
        if (player.chargingSkill != null) {
            game.renderEngine.bindTexture(GuiKnownSkills.GUI);
            game.ingameGUI.drawTexturedModalRect((scaledWidth - 104) / 2, scaledHeight - 70, 0, 134, 104, 10);
            game.ingameGUI.drawTexturedModalRect((scaledWidth - 100) / 2, scaledHeight - 69, 0, 144,
                    (int) (player.chargingSkill.charge * 104 / (player.chargingSkill.getChargeupTime(game.thePlayer) * SkillTickHandler.MULT)), 8);
            game.fontRenderer.drawStringWithShadow(player.chargingSkill.getName(), (scaledWidth - game.fontRenderer.getStringWidth(player.chargingSkill.getName())) / 2, scaledHeight - 69,
                    player.chargingSkill.getNameColour());
        }
    }

    private void drawActiveSkills(int scaledWidth, int scaledHeight) {
        if (player.activeSkills.isEmpty()) {
            return;
        }
        int xOffset;
        Skill skill;
        Iterator<String> itr = player.activeSkills.iterator();
        for (int i = 0; i < player.activeSkills.size(); i++) {
            xOffset = ((scaledWidth - 22 - ((player.activeSkills.size() - 1) * 22)) / 2) + (22 * i);
            skill = SkillRegistry.get(itr.next());
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            game.renderEngine.bindTexture(GuiKnownSkills.GUI);
            game.ingameGUI.drawTexturedModalRect(xOffset, 0, 218, 102, 22, 20);
            game.renderEngine.bindTexture(skill.getTexture());
            GL11.glScalef(0.0625F, 0.0625F, 1F);
            game.ingameGUI.drawTexturedModalRect((xOffset + 3) * 16, 2 * 16, 0, 0, 256, 256);
            GL11.glScalef(16F, 16F, 1F);
            game.fontRenderer.drawStringWithShadow(String.valueOf(skill.timeLeft), xOffset + 19 - game.fontRenderer.getStringWidth(String.valueOf(skill.timeLeft)), 10, 0xFFFFFF);
        }
    }

    private void drawSkillGet(int scaledWidth, int scaledHeight) {
        if (player.skillJustLearnt != null) {
            if (skillGetTimer-- > 0) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
                game.renderEngine.bindTexture(GuiKnownSkills.GUI);
                game.ingameGUI.drawTexturedModalRect(scaledWidth - 150, (scaledHeight - 32) / 2 + (skillGetTimer % 50) - 50, 0, 152, 104, 32);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                game.renderEngine.bindTexture(player.skillJustLearnt.getTexture());
                GL11.glScalef(0.0625F, 0.0625F, 1F);
                game.ingameGUI.drawTexturedModalRect((scaledWidth - 140) * 16, ((scaledHeight - 16) / 2 + (skillGetTimer % 50) - 50) * 16, 0, 0, 256, 256);
                GL11.glScalef(16F, 16F, 1F);
                game.fontRenderer.drawStringWithShadow("Skill Acquired:", scaledWidth - 120, (scaledHeight - 17) / 2 + (skillGetTimer % 50) - 50, -256);
                game.fontRenderer.drawStringWithShadow(player.skillJustLearnt.getName(), scaledWidth - 120, (scaledHeight + 3) / 2 + (skillGetTimer % 50) - 50, player.skillJustLearnt.getNameColour());
            } else {
                player.skillJustLearnt = null;
                SkillPacket pkt = new LearnSkillPacket(game.thePlayer.getEntityId(), null);
                Application.channels.get(pkt.getChannel()).sendToServer(pkt.getPacket(Side.SERVER));
                skillGetTimer = 50;
            }
        }
    }
}
