package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import skillapi.api.gui.base.GuiApi;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.api.gui.component.InputDialogGui;
import skillapi.common.PageHelper;
import skillapi.common.Translation;
import skillapi.packet.*;
import skillapi.packet.base.Packet;
import skillapi.skill.SkillProfile.SkillProfileInfo;
import skillapi.skill.Skills;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
public class SkillProfilesGui extends ItemListGui<SkillProfileInfo> {
    private ButtonComponent addProfileButton;
    private ButtonComponent deleteProfileButton;
    private ButtonComponent editProfileButton;
    private ButtonComponent switchProfileButton;

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SkillProfilesGui(List<SkillProfileInfo> profiles) {
        super(new PageHelper<>(pinCurrentProfile(profiles), SKILL_LIST_SIZE));
        setTitle(Translation.format("skill.gui.profile.title"));
    }

    public static void open() {
        Packet.callback(new GetProfileInfosPacket(), profiles -> GuiApi.displayGui(new SkillProfilesGui(profiles)));
    }

    private static List<SkillProfileInfo> pinCurrentProfile(List<SkillProfileInfo> profiles) {
        SkillProfileInfo info = Skills.getCurrentProfileInfo();
        List<SkillProfileInfo> result = new ArrayList<>();
        result.add(info);
        for (SkillProfileInfo profile : profiles) {
            if (!profile.getName().equals(info.getName())) {
                result.add(profile);
            }
        }
        return result;
    }

    @Override
    protected void init() {
        super.init();

        // Add profile button
        addProfileButton = addButton(this.guiPositionX + 5, this.guiPositionY + 153, 18, 20, "+",
                () -> {
                    String title = Translation.format("skill.gui.profile.dialog.title");
                    String label = Translation.format("skill.gui.profile.dialog.label");
                    InputDialogGui dialog = new InputDialogGui(this, title, label, name -> {
                        Packet.callback(new AddNewProfilePacket(name), this::loadNewData);
                    });
                    GuiApi.displayGui(dialog);
                }
        );
        // Delete profile button
        deleteProfileButton = addButton(this.guiPositionX + 26, this.guiPositionY + 153, 18, 20, "-",
                () -> {
                    SkillProfileInfo selectedItem = getSelectedItem();
                    if (selectedItem != null) {
                        Packet.callback(new RemoveProfilePacket(selectedItem.getName()), this::loadNewData);
                    }
                }
        );
        // Switch profile
        switchProfileButton = addButton(this.guiPositionX + 52, this.guiPositionY + 153, 30, 20, "$skill.constant.switch",
                () -> {
                    SkillProfileInfo selectedItem = getSelectedItem();
                    if (selectedItem != null) {
                        Packet.send(new SwitchProfilePacket(selectedItem.getName()));
                    }
                    SkillProfilesGui.open();
                }
        );
        // Edit profile button
        editProfileButton = addButton(this.guiPositionX + 85, this.guiPositionY + 153, 30, 20, "$skill.constant.edit",
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
        ButtonComponent[] buttons = {this.deleteProfileButton, this.editProfileButton, this.switchProfileButton};
        for (ButtonComponent button : buttons) {
            if (button.getLayout().isIn(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void checkPageStatus() {
        super.checkPageStatus();

        // Check if selected
        if (this.selectedLine == 0 && !this.page.hasPrevPage()) {
            this.editProfileButton.setEnable(false);
            this.deleteProfileButton.setEnable(false);
            this.switchProfileButton.setEnable(false);
        } else {
            this.editProfileButton.setEnable(this.selectedLine != -1);
            this.deleteProfileButton.setEnable(this.editProfileButton.isEnable());
            this.switchProfileButton.setEnable(this.selectedLine != -1);
        }
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
