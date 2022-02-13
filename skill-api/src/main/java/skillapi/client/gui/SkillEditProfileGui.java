package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.RenderUtils;
import skillapi.api.gui.base.listener.MousePressedListener;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.common.PageHelper;
import skillapi.skill.DynamicSkillBuilder;
import skillapi.skill.SkillProfile;
import skillapi.skill.Skills;

import java.awt.*;
import java.util.List;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
public final class SkillEditProfileGui extends ItemListGui implements GuiYesNoCallback {
    private int guiPositionX;
    private int guiPositionY;

    private int skillListPositionX;
    private int skillListPositionY;

    private ButtonComponent nextPageButton;
    private ButtonComponent prevPageButton;

    private ButtonComponent addSkillButton;
    private ButtonComponent deleteSkillButton;
    private ButtonComponent editSkillButton;
    private ButtonComponent applySkillButton;
    private boolean enableApply = false;

    private final PageHelper<DynamicSkillBuilder> page;
    private final SkillProfile editingProfile;

    private int selectedLine = -1;

    public SkillEditProfileGui() {
        this.editingProfile = Skills.getConfigCopy();
        this.page = new PageHelper<>(this.editingProfile.getDynamicSkillBuilders(), SKILL_LIST_SIZE);
    }

    @Override
    protected void init() {
        super.init();

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
        addSkillButton = addButton(this.guiPositionX + 5, this.guiPositionY + 153, 18, 20, "+",
                () -> {
                    DynamicSkillBuilder skillBuilder = new DynamicSkillBuilder();
                    skillBuilder.setName("Unnamed skill #" + skillBuilder.getUniqueId());
                    this.page.addAndToLastPage(skillBuilder);
                }
        );
        // Delete skill button
        deleteSkillButton = addButton(this.guiPositionX + 26, this.guiPositionY + 153, 18, 20, "-",
                () -> getMinecraft().displayGuiScreen(new GuiYesNo(
                        this,
                        I18n.format("skill.gui.config.delete.title", getSelectedSkill().getName()),
                        I18n.format("skill.gui.config.delete.tip"),
                        this.selectedLine
                ))
        );
        // Edit skill button
        editSkillButton = addButton(this.guiPositionX + 85, this.guiPositionY + 153, 30, 20, "$skill.constant.edit",
                () -> displayGui(new SkillEditGui(this, getSelectedSkill()))
        );

        applySkillButton = addButton(this.guiPositionX + 52, this.guiPositionY + 153, 30, 20, "Apply",
                () -> {
                    // TODO 应用方案
                    Skills.serverSwitchConfig(this.editingProfile);
                    this.enableApply = false;
                    checkPageStatus();
                }
        );

        checkPageStatus();
    }

    @Override
    protected boolean keepFocus(int x, int y) {
        return deleteSkillButton.getLayout().isIn(x, y) || editSkillButton.getLayout().isIn(x, y);
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        super.listener(listener);

        MousePressedListener press = (x, y) -> {
            checkPageStatus();
        };

        listener.on(press);
    }

    @Override
    protected void drawBackground() {
        getTextureManager().bindTexture(SkillProfilesGui.SKILL_LIST_TEXTURES);
        RenderUtils.drawTexturedModalRect(guiPositionX, guiPositionY, 0, 0, SKILL_LIST_WIDTH, SKILL_LIST_HEIGHT);
    }

    @Override
    protected void drawItem(int mouseX, int mouseY) {
        List<DynamicSkillBuilder> skills = this.page.getCurrentPage();
        // Draw text
        for (int i = 0; i < skills.size(); i++) {
            getFontRenderer().drawString(skills.get(i).getName(), this.skillListPositionX + 2,
                    this.skillListPositionY + i * SKILL_LIST_ITEM_HEIGHT + 2, Color.WHITE.getRGB());
        }
    }

    @Override
    public void confirmClicked(boolean clickYes, int select) {
        if (clickYes) {
            this.selectedLine = -1;
            DynamicSkillBuilder removed = this.page.removeInCurrentPage(select);
            this.editingProfile.remove(removed);
            this.enableApply = true;
        }
        displayGui(this);
    }

    private void checkPageStatus() {
        this.nextPageButton.setEnable(this.page.hasNextPage());
        this.prevPageButton.setEnable(this.page.hasPrevPage());

        // Check if selected
        this.editSkillButton.setEnable(this.selectedLine != -1);
        this.deleteSkillButton.setEnable(this.editSkillButton.isEnable());
        this.applySkillButton.setEnable(enableApply);
    }

    private DynamicSkillBuilder getSelectedSkill() {
        return this.page.getCurrentPage().get(this.selectedLine);
    }

    public void saveSkill(DynamicSkillBuilder builder) {
        this.editingProfile.put(builder);
        this.enableApply = true;
        // TODO 保存到临时文件
    }
}
