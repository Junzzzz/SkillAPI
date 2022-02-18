package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import skillapi.api.gui.base.GuiApi;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.api.gui.component.InputDialogGui;
import skillapi.common.PageHelper;
import skillapi.common.Translation;
import skillapi.packet.AddNewProfilePacket;
import skillapi.packet.GetProfilePacket;
import skillapi.packet.RemoveProfilePacket;
import skillapi.packet.base.Packet;
import skillapi.skill.SkillProfile.SkillProfileInfo;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
public class SkillProfilesGui extends ItemListGui<SkillProfileInfo> {
    private ButtonComponent addSkillButton;
    private ButtonComponent deleteSkillButton;
    private ButtonComponent editSkillButton;

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SkillProfilesGui(List<SkillProfileInfo> profiles) {
        super(new PageHelper<>(profiles, SKILL_LIST_SIZE));
        setTitle(Translation.format("skill.gui.profile.title"));
    }

    @Override
    protected void init() {
        super.init();

        // Add skill button
        addSkillButton = addButton(this.guiPositionX + 5, this.guiPositionY + 153, 18, 20, "+",
                () -> {
                    String title = Translation.format("skill.gui.profile.dialog.title");
                    String label = Translation.format("skill.gui.profile.dialog.label");
                    InputDialogGui dialog = new InputDialogGui(this, title, label, name -> {
                        Packet.callback(new AddNewProfilePacket(name), this::loadNewData);
                    });
                    GuiApi.displayGui(dialog);
                }
        );
        // Delete skill button
        deleteSkillButton = addButton(this.guiPositionX + 26, this.guiPositionY + 153, 18, 20, "-",
                () -> {
                    SkillProfileInfo selectedItem = getSelectedItem();
                    if (selectedItem != null) {
                        Packet.callback(new RemoveProfilePacket(selectedItem.getName()), this::loadNewData);
                    }
                }
        );
        // Edit skill button
        editSkillButton = addButton(this.guiPositionX + 85, this.guiPositionY + 153, 30, 20, "$skill.constant.edit",
                () -> {
                    SkillProfileInfo info = getSelectedItem();
                    if (info != null) {
                        Packet.callback(new GetProfilePacket(info.getName()), profile -> {
                            // TODO LOCK
                            GuiApi.displayGui(new SkillEditProfileGui(profile));
                        });
                    }
                }
        );

        checkPageStatus();
    }

    @Override
    protected boolean keepFocus(int x, int y) {
        return deleteSkillButton.getLayout().isIn(x, y) || editSkillButton.getLayout().isIn(x, y);
    }

    @Override
    protected void checkPageStatus() {
        super.checkPageStatus();

        // Check if selected
        this.editSkillButton.setEnable(this.selectedLine != -1);
        this.deleteSkillButton.setEnable(this.editSkillButton.isEnable());
    }

    private SkillProfileInfo getSelectedItem() {
        List<SkillProfileInfo> currentPage = page.getCurrentPage();

        if (0 <= this.selectedLine && this.selectedLine < currentPage.size()) {
            return currentPage.get(this.selectedLine);
        }

        return null;
    }

    @Override
    protected void drawItem(int mouseX, int mouseY) {
        List<SkillProfileInfo> profiles = this.page.getCurrentPage();

        // Draw text
        FontRenderer fontRenderer = getFontRenderer();
        for (int i = 0; i < profiles.size(); i++) {
            SkillProfileInfo info = profiles.get(i);
            int x = this.skillListPositionX + 2;
            int y = this.skillListPositionY + i * SKILL_LIST_ITEM_HEIGHT + 2;
            fontRenderer.drawString(info.getName(), x, y, Color.WHITE.getRGB());
            fontRenderer.drawString(info.getLastUpdater(), x, y + 7, Color.LIGHT_GRAY.getRGB());
            LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(info.getLastUpdateTime()), ZoneId.systemDefault());
            String updateTime = time.format(timeFormatter);
            fontRenderer.drawString(
                    updateTime,
                    x + SKILL_LIST_ITEM_WIDTH - fontRenderer.getStringWidth(updateTime) - 3, y + 7,
                    Color.LIGHT_GRAY.getRGB()
            );
        }
    }

    private void loadNewData(List<SkillProfileInfo> newData) {
        this.page.setData(newData);
        this.page.toLastPage();
        if (getSelectedItem() == null) {
            this.selectedLine = -1;
        }
        checkPageStatus();
    }
}
