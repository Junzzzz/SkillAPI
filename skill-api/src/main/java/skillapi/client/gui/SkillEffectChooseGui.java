package skillapi.client.gui;

import lombok.var;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.client.gui.component.SkillEffectListComponent;
import skillapi.skill.SkillEffectHandler;
import skillapi.utils.ClassUtils;

import java.util.ArrayList;
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
        final int listWidth = (width - 10 - 10 - 30) / 2;

//        // TODO TEST
        final var test = SkillEffectHandler.getEffects();
        test.addAll(new ArrayList<>(test));
        test.addAll(new ArrayList<>(test));
//        this.leftSelectList = new SkillEffectListGui(this,
//                ,
//                test,
//                (index, dClk) -> addButton.setEnabled(index != -1)
//        );
//        this.leftSelectList = new SkillEffectListGui(this,
//                new GuiConfig(10, 30, listWidth, height - 30 - 40),
//                SkillEffectHandler.getEffects().stream()
//                        .filter(e -> !parent.effectListGui.getEffects().contains(e))
//                        .collect(Collectors.toList()),
//                (index, dClk) -> addButton.setEnabled(index != -1)
//        );
//        this.rightSelectList = new SkillEffectListGui(this,
//                new GuiBox(width - listWidth - 10, 30, listWidth, height - 30 - 40),
//                parent.effectListGui.getEffects(),
//                (index, dClk) -> removeButton.setEnabled(index != -1)
//        );

        final Layout rightListBox = new Layout(width - listWidth - 10, 30, listWidth, height - 30 - 40);
        final Layout leftListBox = new Layout(10, 30, listWidth, height - 30 - 40);

        this.leftSelectList = new SkillEffectListComponent(leftListBox, 25, test);
        this.rightSelectList = new SkillEffectListComponent(rightListBox, 25, parent.effectListGui.getEffects());

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
        addComponent(leftSelectList);
        addComponent(rightSelectList);
        checkButtonStatus();
    }

    private void checkButtonStatus() {
//        this.removeButton.setEnabled(this.rightSelectList.canRemoveSelected());
//        this.addButton.setEnabled(this.leftSelectList.canRemoveSelected());
//        this.finishButton.setEnabled(this.rightSelectList.getSize() > 0);
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);

//        this.leftSelectList.drawScreen(mouseX, mouseY, partialTicks);
//        this.leftSelectList.overlay();
//        this.rightSelectList.drawScreen(mouseX, mouseY, partialTicks);
//        this.rightSelectList.overlay();

        this.drawCenteredString("Choose Effect", width / 2, 15, 0xFFFFFF);
    }
}
