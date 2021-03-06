package skillapi.client.gui;

import lombok.val;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.client.gui.component.SkillEffectListComponent;
import skillapi.skill.SkillEffectHandler;
import skillapi.utils.ClassUtils;

import java.util.stream.Collectors;

/**
 * @author Jun
 * @date 2020/11/13.
 */
public class SkillEffectChooseGui extends BaseGui {
    private final SkillEditGui parent;

    private SkillEffectListComponent leftSelectList;
    private SkillEffectListComponent rightSelectList;

    private Button addButton;
    private Button removeButton;
    private Button finishButton;

    public SkillEffectChooseGui(SkillEditGui parent) {
        this.parent = parent;
    }

    @Override
    protected void init() {
        removeButton = addButton(width / 2 - 10, height / 3 - 10, 20, 20, "<<", () -> {
            this.leftSelectList.add(this.rightSelectList.removeSelected());
            checkButtonStatus();
        });
        addButton = addButton(width / 2 - 10, height / 3 * 2 - 10, 20, 20, ">>", () -> {
            this.rightSelectList.add(this.leftSelectList.removeSelected());
            checkButtonStatus();
        });

        finishButton = addButton(width / 2 - 40, height - 30, 80, 20, "$gui.done", () -> {
            parent.selectedEffects.clear();
            parent.selectedEffects.addAll(
                    this.rightSelectList.getEffects().stream()
                            .map(a -> ClassUtils.newEmptyInstance(a, "Failed to instantiate skill effect: %s", a.getName()))
                            .collect(Collectors.toList())
            );
            displayGui(parent);
        });

        final int listWidth = (width - 10 - 10 - 30) / 2;

        final Layout rightListBox = new Layout(width - listWidth - 10, 30, listWidth, height - 30 - 40);
        final Layout leftListBox = new Layout(10, 30, listWidth, height - 30 - 40);

        val leftList = SkillEffectHandler.getEffects().stream()
                .filter(e -> !parent.effectListGui.getEffects().contains(e))
                .collect(Collectors.toList());

        this.leftSelectList = new SkillEffectListComponent(leftListBox, 25, leftList);
        this.rightSelectList = new SkillEffectListComponent(rightListBox, 25, parent.effectListGui.getEffects());

        this.leftSelectList.setAssociatedButton(addButton);
        this.rightSelectList.setAssociatedButton(removeButton);

        addComponent(leftSelectList);
        addComponent(rightSelectList);
        checkButtonStatus();
    }

    private void checkButtonStatus() {
        this.removeButton.setEnabled(this.rightSelectList.hasSelected());
        this.addButton.setEnabled(this.leftSelectList.hasSelected());
        this.finishButton.setEnabled(this.rightSelectList.getListSize() > 0);
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);

        this.drawCenteredString("Choose Effect", width / 2, 15, 0xFFFFFF);
    }
}
