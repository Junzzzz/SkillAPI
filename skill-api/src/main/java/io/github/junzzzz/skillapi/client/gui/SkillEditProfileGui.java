package io.github.junzzzz.skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.junzzzz.skillapi.api.gui.base.ListenerRegistry;
import io.github.junzzzz.skillapi.api.gui.base.listener.MousePressedListener;
import io.github.junzzzz.skillapi.api.gui.component.ButtonComponent;
import io.github.junzzzz.skillapi.common.PageHelper;
import io.github.junzzzz.skillapi.common.Translation;
import io.github.junzzzz.skillapi.packet.ProfileSavePacket;
import io.github.junzzzz.skillapi.packet.base.Packet;
import io.github.junzzzz.skillapi.skill.DynamicSkillBuilder;
import io.github.junzzzz.skillapi.skill.SkillProfile;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.util.List;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
public final class SkillEditProfileGui extends ItemListGui<DynamicSkillBuilder> implements GuiYesNoCallback {
    private ButtonComponent backSkillButton;
    private ButtonComponent nextPageButton;
    private ButtonComponent prevPageButton;

    private ButtonComponent addSkillButton;
    private ButtonComponent deleteSkillButton;
    private ButtonComponent editSkillButton;

    private final SkillProfile editingProfile;

    public SkillEditProfileGui(SkillProfile profile) {
        super(new PageHelper<>(profile.getDynamicSkillBuilders(), SKILL_LIST_SIZE));
        setTitle(Translation.format("skill.gui.editProfile.title", profile.getName()));
        this.editingProfile = profile;
    }

    @Override
    protected void init() {
        super.init();

        backSkillButton = addButton(this.guiPositionX - 23, this.guiPositionY, 20, 20, "<<", SkillProfilesGui::open);

        // Add skill button
        addSkillButton = addButton(this.guiPositionX + 5, this.guiPositionY + 153, 18, 20, "+",
                () -> {
                    DynamicSkillBuilder skillBuilder = new DynamicSkillBuilder(editingProfile);
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

        checkPageStatus();
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
    protected boolean keepFocus(int x, int y) {
        return deleteSkillButton.getLayout().isIn(x, y) || editSkillButton.getLayout().isIn(x, y);
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
            saveAll();
        }
        displayGui(this);
    }

    @Override
    protected void checkPageStatus() {
        super.checkPageStatus();

        // Check if selected
        this.editSkillButton.setEnable(this.selectedLine != -1);
        this.deleteSkillButton.setEnable(this.editSkillButton.isEnable());
    }

    private DynamicSkillBuilder getSelectedSkill() {
        return this.page.getCurrentPage().get(this.selectedLine);
    }

    public void saveSkill(DynamicSkillBuilder builder) {
        this.editingProfile.put(builder);

        saveAll();
    }

    private void saveAll() {
        // Save on server
        Packet.send(new ProfileSavePacket(this.editingProfile));
    }
}
