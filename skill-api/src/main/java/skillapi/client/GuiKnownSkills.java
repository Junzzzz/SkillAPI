package skillapi.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import skillapi.Application;
import skillapi.PlayerSkills;
import skillapi.Skill;
import skillapi.SkillRegistry;
import skillapi.api.gui.base.GuiApi;
import skillapi.client.gui.SkillConfigGui;
import skillapi.client.gui.TestGui;
import skillapi.packets.SkillPacket;
import skillapi.packets.UpdateSkillPacket;

@SideOnly(Side.CLIENT)
public final class GuiKnownSkills extends GuiScreen {
    public static final ResourceLocation GUI = new ResourceLocation(Application.MOD_ID, "textures/gui/skill-gui.png");
    private final PlayerSkills skills;
    private final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
    private final String[] skillKeys = new String[5];
    private boolean isScrollPressed = false;
    private int scrollPos = 0; //up to 93
    private Skill heldSkill;

    public GuiKnownSkills(PlayerSkills player) {
        super();
        this.skills = player;
        allowUserInput = true;
        for (int i = 0; i < skillKeys.length; i++) {
            skillKeys[i] = Keyboard.getKeyName(SkillAPIClientProxy.skillKeyBindings[i].getKeyCode());
        }
    }

    @Override
    public void initGui() {
        buttonList.clear();
        this.buttonList.add(new GuiButton(1, (width - 206) / 2 - 20, (height - 134) / 2, 20, 20, "</>"));
        this.buttonList.add(new GuiButton(2, (width - 206) / 2 - 20, (height - 134) / 2 + 25, 20, 20, "<?>"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glEnable(GL11.GL_BLEND);
        drawDefaultBackground();
        drawGUIBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawScroll(mouseY);
        drawSkillBar(mouseX, mouseY);
        drawSkillList(mouseX, mouseY);
        drawHeldSkill(mouseX, mouseY);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawGUIBackground() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(GUI);
        drawTexturedModalRect((width - 206) / 2, (height - 134) / 2, 0, 0, 206, 134);
        fontRendererObj.drawStringWithShadow(I18n.format("skill.gui.title"), width / 2 - 73, height / 2 - 61, 0xFCFC80);
        for (int i = 0; i < skillKeys.length; i++) {
            fontRendererObj.drawString(skillKeys[i], width / 2 - 90, (height / 2 - 40) + (21 * i), 0xE2E2E9);
        }
    }

    private void drawSkillBar(int mouseX, int mouseY) {
        int posX, posY;
        for (int i = 0; i < skills.skillBar.length; i++) {
            if (skills.skillBar[i] != null) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.renderEngine.bindTexture(skills.skillBar[i].getTexture());
                GL11.glScalef(0.0625F, 0.0625F, 1F);
                drawTexturedModalRect((posX = (width / 2 - 96)) * 16, (posY = ((height / 2 - 45) + (21 * i))) * 16, 0, 0, 256, 256);
                GL11.glScalef(16F, 16F, 1F);
                if (!Mouse.isButtonDown(0) && isMouseOverArea(mouseX, mouseY, posX, posY, 16, 16)) {
                    drawGradientRect(posX, posY, posX + 16, posY + 16, 0x80BCC4D0, 0x80293445);
                }
            }
        }
    }

    private void drawSkillList(int mouseX, int mouseY) {
        int posX, posY, offset = Math.round((scrollPos * (skills.knownSkills.size() - 5)) / 93);
        offset = Math.max(offset, 0);
        for (int i = offset; i < offset + 5 && i < skills.knownSkills.size(); i++) {
            drawSkillInfo(SkillRegistry.get(skills.knownSkills.get(i)), i, posX = (width / 2 - 71), posY = (height / 2 - 45 + (21 * (i - offset))));
            if (!Mouse.isButtonDown(0) && isMouseOverArea(mouseX, mouseY, posX, posY, 16, 16)) {
                drawGradientRect(posX, posY, posX + 16, posY + 16, 0x80BCC4D0, 0x80293445);
            }
        }
        for (int i = offset; i < offset + 5 && i < skills.knownSkills.size(); i++) {
            if (!Mouse.isButtonDown(0) && isMouseOverArea(mouseX, mouseY, width / 2 - 71, height / 2 - 45 + (21 * (i - offset)), 16, 16)) {
                drawToolTip(SkillRegistry.get(skills.knownSkills.get(i)), mouseX, mouseY);
            }
        }
    }

    private void drawSkillInfo(Skill skill, int i, int x, int y) {
        if (skill != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(SkillRegistry.get(skills.knownSkills.get(i)).getTexture());
            GL11.glScalef(0.0625F, 0.0625F, 1F);
            drawTexturedModalRect(x * 16, y * 16, 0, 0, 256, 256);
            GL11.glScalef(16F, 16F, 1F);
            fontRendererObj.drawStringWithShadow(skill.getLocalizedName(), x + 20, y, skill.getNameColour());
            drawSkillStats(skill, x + 20, y + 9);
        }
    }

    private void drawSkillStats(Skill skill, int x, int y) {
        if (skill.getManaCost(player) > 0) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(GUI);
            drawTexturedModalRect(x, y, 104, 134, 9, 9);
            fontRendererObj.drawString(String.valueOf(skill.getManaCost(player)), x + 10, y + 1, 0xCFFFDF);
            x += 25;
        }
        if (skill.getChargeupTime(player) > 0) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(GUI);
            drawTexturedModalRect(x, y, 113, 134, 9, 9);
            fontRendererObj.drawString(String.valueOf(skill.getChargeupTime(player)), x + 10, y + 1, 0xCFFFDF);
            x += 34;
        }
        if (skill.getCooldownTime(player) > 0) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(GUI);
            drawTexturedModalRect(x, y, 122, 134, 9, 9);
            fontRendererObj.drawString(String.valueOf(skill.getCooldownTime(player)), x + 10, y + 1, 0xCFFFDF);
            x += 34;
        }
        if (skill.getDuration(player) > 0) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(GUI);
            drawTexturedModalRect(x, y, 131, 134, 9, 9);
            fontRendererObj.drawString(String.valueOf(skill.getDuration(player)), x + 10, y + 1, 0xCFFFDF);
        }
    }

    private void drawToolTip(Skill skill, int mouseX, int mouseY) {
        if (skill != null) {
            mouseX += 9;
            String[] desc = skill.getDescription().split("\\\\n");
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
            mc.renderEngine.bindTexture(GUI);
            drawTexturedModalRect(mouseX, mouseY, 0, 184, 175, 15);
            for (int i = 0; i < desc.length; i++) {
                drawTexturedModalRect(mouseX, mouseY + 15 + (9 * i), 0, 188, 175, 9);
            }
            drawTexturedModalRect(mouseX, mouseY + 15 + (9 * desc.length), 0, 198, 175, 3);
            GL11.glDisable(GL11.GL_BLEND);
            fontRendererObj.drawStringWithShadow(skill.getType(), mouseX + 5, mouseY + 5, 0xE0BC38);
            for (int i = 0; i < desc.length; i++) {
                fontRendererObj.drawString(desc[i], mouseX + 5, mouseY + 15 + (9 * i), 0x8FA8FF);
            }
        }
    }

    private void drawScroll(int mouseY) {
        if (isScrollPressed) {
            scrollPos = mouseY - 7 - (height / 2 - 49);
            scrollPos = Math.max(scrollPos, 0);
            scrollPos = Math.min(scrollPos, 93);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(GUI);
        drawTexturedModalRect(width / 2 + 83, height / 2 - 49 + scrollPos, 206, 0, 12, 15);
    }

    private void drawHeldSkill(int mouseX, int mouseY) {
        if (heldSkill != null) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(heldSkill.getTexture());
            GL11.glScalef(0.0625F, 0.0625F, 1F);
            drawTexturedModalRect((mouseX - 8) * 16, (mouseY - 8) * 16, 0, 0, 256, 256);
            GL11.glScalef(16F, 16F, 1F);
        }
    }

    private boolean isMouseOverArea(int mouseX, int mouseY, int posX, int posY, int sizeX, int sizeY) {
        return (mouseX >= posX && mouseX < posX + sizeX && mouseY >= posY && mouseY < posY + sizeY);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int wheelState = Mouse.getEventDWheel();
        if (wheelState != 0) {
            scrollPos += wheelState > 0 ? -8 : 8;
            scrollPos = Math.max(scrollPos, 0);
            scrollPos = Math.min(scrollPos, 93);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            GuiApi.displayGui(new SkillConfigGui());
        } else if (button.id == 2) {
            GuiApi.displayGui(new TestGui());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (button != 0) {
            return;
        }
        if (isMouseOverArea(mouseX, mouseY, width / 2 + 83, height / 2 - 49, 12, 108)) {
            isScrollPressed = true;
            return;
        }
        for (int i = 0; i < skills.skillBar.length; i++) {
            if (skills.skillBar[i] != null && isMouseOverArea(mouseX, mouseY, width / 2 - 96, (height / 2 - 45) + (21 * i), 16, 16)) {
                heldSkill = skills.skillBar[i];
                sendSkillUpdate(i, null);
                return;
            }
        }
        int offset = Math.round((scrollPos * (skills.knownSkills.size() - 5)) / 93);
        offset = Math.max(offset, 0);
        for (int i = offset; i < offset + 5 && i < skills.knownSkills.size(); i++) {
            if (isMouseOverArea(mouseX, mouseY, width / 2 - 71, height / 2 - 45 + (21 * (i - offset)), 16, 16) && skills.knownSkills.get(i) != null) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                    for (int j = 0; j < skills.skillBar.length; j++) {
                        if (skills.skillBar[j] == null) {
                            sendSkillUpdate(j, skills.knownSkills.get(i));
                            return;
                        }
                    }
                }
                heldSkill = SkillRegistry.get(skills.knownSkills.get(i));
                return;
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        if (!Mouse.isButtonDown(0)) {
            isScrollPressed = false;
            if (heldSkill != null) {
                for (int i = 0; i < skills.skillBar.length; i++) {
                    if (isMouseOverArea(mouseX, mouseY, width / 2 - 96, (height / 2 - 45) + (21 * i), 16, 16)) {
                        sendSkillUpdate(i, heldSkill.getName());
                        break;
                    }
                }
                heldSkill = null;
            }
        }
    }

    private void sendSkillUpdate(int i, String skill) {
        SkillPacket pkt = new UpdateSkillPacket(player.getEntityId(), i, skill);
        Application.channels.get(pkt.getChannel()).sendToServer(pkt.getPacket(Side.SERVER));
    }

    @Override
    protected void keyTyped(char key, int keyCode) {
        if (keyCode == 1 || keyCode == SkillAPIClientProxy.skillGuiKeyBinding.getKeyCode()) {
            mc.thePlayer.closeScreen();
        }
    }

    @Override
    public void onGuiClosed() {
        heldSkill = null;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}