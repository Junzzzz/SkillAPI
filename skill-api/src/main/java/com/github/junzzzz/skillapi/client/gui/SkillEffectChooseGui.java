package com.github.junzzzz.skillapi.client.gui;

import com.github.junzzzz.skillapi.api.gui.base.BaseGui;
import com.github.junzzzz.skillapi.api.gui.base.Layout;
import com.github.junzzzz.skillapi.api.gui.base.ListenerRegistry;
import com.github.junzzzz.skillapi.api.gui.component.ButtonComponent;
import com.github.junzzzz.skillapi.client.gui.component.SkillEffectListComponent;
import com.github.junzzzz.skillapi.common.Translation;
import com.github.junzzzz.skillapi.skill.SkillEffect;
import com.github.junzzzz.skillapi.skill.Skills;
import com.github.junzzzz.skillapi.skill.UniversalParam;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jun
 */
public class SkillEffectChooseGui extends BaseGui {
    private final SkillEditGui parent;

    private SkillEffectListComponent leftSelectList;
    private SkillEffectListComponent rightSelectList;

    private ButtonComponent addButton;
    private ButtonComponent removeButton;
    private ButtonComponent finishButton;

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
            List<Class<? extends SkillEffect>> effectClasses = new ArrayList<>();
            for (String s : this.rightSelectList.getList()) {
                effectClasses.add(Skills.getSkillEffect(s));
            }
            parent.skillBuilder.setEffects(effectClasses);
            parent.saveSkill();
            displayGui(parent);
        });

        final int listWidth = (width - 10 - 10 - 30) / 2;

        final Layout rightListBox = new Layout(width - listWidth - 10, 30, listWidth, height - 30 - 40);
        final Layout leftListBox = new Layout(10, 30, listWidth, height - 30 - 40);

        val rightList = parent.effectList.getList().stream()
                .filter(e -> e != UniversalParam.INSTANCE)
                .map(SkillEffect::getUnlocalizedName)
                .collect(Collectors.toList());

        val leftList = Skills.getEffectNames().stream()
                .filter(e -> !rightList.contains(e))
                .collect(Collectors.toList());

        this.leftSelectList = new SkillEffectListComponent(leftListBox, 25, leftList);
        this.rightSelectList = new SkillEffectListComponent(rightListBox, 25, rightList);

        this.leftSelectList.setAssociatedButton(addButton);
        this.rightSelectList.setAssociatedButton(removeButton);

        addComponent(leftSelectList);
        addComponent(rightSelectList);
        checkButtonStatus();
    }

    private void checkButtonStatus() {
        this.removeButton.setEnable(this.rightSelectList.hasSelected());
        this.addButton.setEnable(this.leftSelectList.hasSelected());
        this.finishButton.setEnable(this.rightSelectList.getListSize() > 0);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground();

        this.drawCenteredString(Translation.format("skill.gui.effectChoose.title"), width / 2, 15, 0xFFFFFF);
    }

    @Override
    protected void listener(ListenerRegistry listener) {

    }
}
