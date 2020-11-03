package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import skillapi.Application;
import skillapi.common.PageHelper;
import skillapi.skill.SkillConfig;
import skillapi.skill.SkillConfig.DynamicSkillConfig;

import java.awt.*;
import java.util.List;

/**
 * @author Jun
 * @date 2020/10/4.
 */
@SideOnly(Side.CLIENT)
public final class SkillConfigGui extends GuiScreen implements GuiYesNoCallback {
    protected static final ResourceLocation SKILL_LIST_TEXTURES = new ResourceLocation(Application.MOD_ID, "textures/gui/skill-list.png");

    private static final int SKILL_LIST_WIDTH = 121;
    private static final int SKILL_LIST_HEIGHT = 169;

    private static final int SKILL_LIST_ITEM_X = 6;
    private static final int SKILL_LIST_ITEM_Y = 10;

    private static final int SKILL_LIST_ITEM_WIDTH = 108;
    private static final int SKILL_LIST_ITEM_HEIGHT = 19;
    private static final int SKILL_LIST_ITEM_HEIGHT_ALL = 19 * 7;

    private int guiPositionX;
    private int guiPositionY;

    private int skillListPositionX;
    private int skillListPositionY;

    private GuiButton nextPageButton;
    private GuiButton prevPageButton;

    private GuiButton addSkillButton;
    private GuiButton deleteSkillButton;
    private GuiButton editSkillButton;

    private final PageHelper<DynamicSkillConfig> page = new PageHelper<>(SkillConfig.SERVER_CONFIG.getCustoms(), 7);

    private int selectedLine = -1;

    @Override
    public void initGui() {
        this.guiPositionX = (this.width - SKILL_LIST_WIDTH) / 2;
        this.guiPositionY = (this.height - SKILL_LIST_HEIGHT - 25) / 2;

        this.skillListPositionX = this.guiPositionX + SKILL_LIST_ITEM_X;
        this.skillListPositionY = this.guiPositionY + SKILL_LIST_ITEM_Y;

        this.prevPageButton = new GuiButton(1, this.guiPositionX, this.guiPositionY + SKILL_LIST_HEIGHT, 20, 20, "<");
        this.nextPageButton = new GuiButton(2, this.guiPositionX + SKILL_LIST_WIDTH - 20, this.guiPositionY + SKILL_LIST_HEIGHT, 20, 20, ">");

        this.addSkillButton = new GuiButton(3, this.guiPositionX + 5, this.guiPositionY + 145, 18, 20, "+");
        this.deleteSkillButton = new GuiButton(4, this.guiPositionX + 26, this.guiPositionY + 145, 18, 20, "-");
        this.editSkillButton = new GuiButton(5, this.guiPositionX + 85, this.guiPositionY + 145, 30, 20, "Edit");

        super.buttonList.clear();

        super.buttonList.add(this.nextPageButton);
        super.buttonList.add(this.prevPageButton);
        super.buttonList.add(this.addSkillButton);
        super.buttonList.add(this.deleteSkillButton);
        super.buttonList.add(this.editSkillButton);

        checkPageStatus();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glEnable(GL11.GL_BLEND);
        super.drawDefaultBackground();

        this.mc.getTextureManager().bindTexture(SKILL_LIST_TEXTURES);
        drawBackground();
        drawSkill(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);

        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawBackground() {
        this.drawTexturedModalRect(guiPositionX, guiPositionY, 0, 0, SKILL_LIST_WIDTH, SKILL_LIST_HEIGHT);
    }

    private void drawSkill(int mouseX, int mouseY) {

        final List<DynamicSkillConfig> skills = this.page.getCurrentPage();

        // Draw background
        for (int i = 0; i < skills.size(); i++) {
            this.drawTexturedModalRect(this.skillListPositionX, this.skillListPositionY + i * SKILL_LIST_ITEM_HEIGHT, 0, 169, SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT);
        }

        final int mouseOver = getMouseOver(mouseX, mouseY);
        if (mouseOver != -1 && mouseOver < skills.size()) {
            this.drawTexturedModalRect(this.skillListPositionX, this.skillListPositionY + mouseOver * SKILL_LIST_ITEM_HEIGHT, 0, 207, SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT);
        }

        if (selectedLine != -1) {
            this.drawTexturedModalRect(this.skillListPositionX, this.skillListPositionY + selectedLine * SKILL_LIST_ITEM_HEIGHT, 108, 169, SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT);
        }

        // Draw text
        for (int i = 0; i < skills.size(); i++) {
            this.fontRendererObj.drawString(skills.get(i).getName(), this.skillListPositionX + 2, this.skillListPositionY + i * SKILL_LIST_ITEM_HEIGHT + 2, Color.WHITE.getRGB());
        }
    }

    @Override
    public void confirmClicked(boolean clickYes, int select) {
        if (clickYes) {
            this.page.removeInCurrentPage(select);
        }
        this.mc.displayGuiScreen(this);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == nextPageButton.id) {
            this.page.nextPage();
        } else if (button.id == prevPageButton.id) {
            this.page.prevPage();
        } else if (button.id == addSkillButton.id) {
            this.page.addAndToLastPage(new DynamicSkillConfig("Temporary skill name#" + this.page.dataSize()));
        } else if (button.id == deleteSkillButton.id) {
            this.mc.displayGuiScreen(new GuiYesNo(
                    this,
                    I18n.format("skill.gui.config.delete.title", getSelectedSkill().getName()),
                    I18n.format("skill.gui.config.delete.tip"),
                    this.selectedLine
            ));
        } else if (button.id == editSkillButton.id) {
            this.mc.displayGuiScreen(new SkillEditGui(this));
        }
    }

    private void checkPageStatus() {
        this.nextPageButton.enabled = this.page.hasNextPage();
        this.prevPageButton.enabled = this.page.hasPrevPage();

        // Check if selected
        this.editSkillButton.enabled = this.selectedLine != -1;
        this.deleteSkillButton.enabled = this.editSkillButton.enabled;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (button != 0) {
            return;
        }

        this.selectedLine = getMouseOver(mouseX, mouseY);
        if (this.selectedLine >= this.page.getCurrentPage().size()) {
            this.selectedLine = -1;
        }

        checkPageStatus();
    }

    /**
     * Get the line where the mouse is hovering
     *
     * @param mouseX Mouse x axis
     * @param mouseY Mouse y axis
     * @return Selected row. If not selected, return {@code -1}
     */
    private int getMouseOver(int mouseX, int mouseY) {
        final int x = mouseX - this.skillListPositionX;
        final int y = mouseY - this.skillListPositionY;
        if (x > 0 && y > 0 && x < SKILL_LIST_ITEM_WIDTH && y < SKILL_LIST_ITEM_HEIGHT_ALL) {
            return y / SKILL_LIST_ITEM_HEIGHT;
        }
        return -1;
    }

    private DynamicSkillConfig getSelectedSkill() {
        return this.page.getCurrentPage().get(this.selectedLine);
    }
}
