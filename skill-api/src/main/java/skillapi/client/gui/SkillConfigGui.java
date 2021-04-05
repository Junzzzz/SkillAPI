package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import skillapi.Application;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.RenderUtils;
import skillapi.api.gui.base.listener.MousePressedListener;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.common.PageHelper;
import skillapi.skill.SkillLocalConfig;
import skillapi.skill.SkillLocalConfig.DynamicSkillConfig;

import java.awt.*;
import java.util.List;

/**
 * @author Jun
 * @date 2020/10/4.
 */
@SideOnly(Side.CLIENT)
public final class SkillConfigGui extends BaseGui implements GuiYesNoCallback {
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

    private ButtonComponent nextPageButton;
    private ButtonComponent prevPageButton;

    private ButtonComponent addSkillButton;
    private ButtonComponent deleteSkillButton;
    private ButtonComponent editSkillButton;

    private final PageHelper<DynamicSkillConfig> page = new PageHelper<>(SkillLocalConfig.SERVER_CONFIG.getCustoms(),
            7);

    private int selectedLine = -1;

    @Override
    protected void init() {
        this.guiPositionX = (this.width - SKILL_LIST_WIDTH) / 2;
        this.guiPositionY = (this.height - SKILL_LIST_HEIGHT - 25) / 2;

        this.skillListPositionX = this.guiPositionX + SKILL_LIST_ITEM_X;
        this.skillListPositionY = this.guiPositionY + SKILL_LIST_ITEM_Y;

        // Prev page button
        prevPageButton = addButton(this.guiPositionX, this.guiPositionY + SKILL_LIST_HEIGHT, 20, 20, "<",
                () -> {
                    this.page.prevPage();
                    checkPageStatus();
                }
        );
        // Next page button
        nextPageButton = addButton(this.guiPositionX + SKILL_LIST_WIDTH - 20, this.guiPositionY + SKILL_LIST_HEIGHT, 20, 20, ">",
                () -> {
                    this.page.nextPage();
                    checkPageStatus();
                });
        // Add skill button
        addSkillButton = addButton(this.guiPositionX + 5, this.guiPositionY + 145, 18, 20, "+",
                () -> this.page.addAndToLastPage(new DynamicSkillConfig("Temporary skill name#" + this.page.dataSize()))
        );
        // Delete skill button
        deleteSkillButton = addButton(this.guiPositionX + 26, this.guiPositionY + 145, 18, 20, "-",
                () -> getMinecraft().displayGuiScreen(new GuiYesNo(
                        this,
                        I18n.format("skill.gui.config.delete.title", getSelectedSkill().getName()),
                        I18n.format("skill.gui.config.delete.tip"),
                        this.selectedLine
                ))
        );
        // Edit skill button
        editSkillButton = addButton(this.guiPositionX + 85, this.guiPositionY + 145, 30, 20, "Edit",
                () -> displayGui(new SkillEditGui(this))
        );

        checkPageStatus();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        GL11.glEnable(GL11.GL_BLEND);
        super.drawDefaultBackground();

        getTextureManager().bindTexture(SKILL_LIST_TEXTURES);
        drawBackground();
        drawSkill(mouseX, mouseY);

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        MousePressedListener press = (x, y) -> {
            // Do not lose focus when deleting and editing
            if (!deleteSkillButton.getLayout().isIn(x, y) && !editSkillButton.getLayout().isIn(x, y)) {
                this.selectedLine = getMouseOver(x, y);
                if (this.selectedLine >= this.page.getCurrentPage().size()) {
                    this.selectedLine = -1;
                }
            }

            checkPageStatus();
        };

        listener.on(press);
    }

    @Override
    protected void drawBackground() {
        RenderUtils.drawTexturedModalRect(guiPositionX, guiPositionY, 0, 0, SKILL_LIST_WIDTH, SKILL_LIST_HEIGHT);
    }

    private void drawSkill(int mouseX, int mouseY) {
        final List<DynamicSkillConfig> skills = this.page.getCurrentPage();

        // Draw background
        for (int i = 0; i < skills.size(); i++) {
            RenderUtils.drawTexturedModalRect(this.skillListPositionX, this.skillListPositionY + i * SKILL_LIST_ITEM_HEIGHT, 0, 169, SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT);
        }

        final int mouseOver = getMouseOver(mouseX, mouseY);
        if (mouseOver != -1 && mouseOver < skills.size()) {
            RenderUtils.drawTexturedModalRect(this.skillListPositionX, this.skillListPositionY + mouseOver * SKILL_LIST_ITEM_HEIGHT, 0, 207, SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT);
        }

        if (selectedLine != -1) {
            RenderUtils.drawTexturedModalRect(this.skillListPositionX, this.skillListPositionY + selectedLine * SKILL_LIST_ITEM_HEIGHT, 108, 169, SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT);
        }

        // Draw text
        for (int i = 0; i < skills.size(); i++) {
           getFontRenderer().drawString(skills.get(i).getName(), this.skillListPositionX + 2, this.skillListPositionY + i * SKILL_LIST_ITEM_HEIGHT + 2, Color.WHITE.getRGB());
        }
    }

    @Override
    public void confirmClicked(boolean clickYes, int select) {
        if (clickYes) {
            this.page.removeInCurrentPage(select);
        }
        displayGui(this);
    }

    private void checkPageStatus() {
        this.nextPageButton.setEnable(this.page.hasNextPage());
        this.prevPageButton.setEnable(this.page.hasPrevPage());

        // Check if selected
        this.editSkillButton.setEnable(this.selectedLine != -1);
        this.deleteSkillButton.setEnable(this.editSkillButton.isEnable());
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
