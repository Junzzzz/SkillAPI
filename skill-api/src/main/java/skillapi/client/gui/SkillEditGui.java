package skillapi.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.val;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.CachedTexture;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.component.SliderComponent;
import skillapi.api.gui.component.impl.ScrollingListComponent;
import skillapi.skill.SkillEffectBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 * @date 2020/10/24.
 */
@SideOnly(Side.CLIENT)
public final class SkillEditGui extends BaseGui {
    private final SkillConfigGui parent;
    private int formContentHeight;
    private CachedTexture formTexture;
    private Layout formLayout;
    private SliderComponent slider;
    protected ScrollingListComponent<SkillEffectBuilder> effectList;

    protected final List<SkillEffectBuilder> selectedEffects = new ArrayList<>();

    public SkillEditGui(SkillConfigGui parent) {
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.formLayout = new Layout(110, 10, this.width - 136, this.height - 40);

        final Layout listLayout = new Layout(10, 10, 100, this.height - 10 - 30);
        ScrollingListComponent.SlotRenderer<SkillEffectBuilder> renderer = (data, x, y) -> {
            drawString(data.getName(), x, y, 0xFFFFFF);
        };

        this.effectList = new ScrollingListComponent<>(listLayout, 25, selectedEffects, renderer);
        this.effectList.setClickEvent((item, index) -> {
            if (this.formTexture != null) {
                this.formTexture.delete();
            }
            this.formContentHeight = item.getParamSize() * 25;
            this.formTexture = new CachedTexture(this.width - 130, this.formContentHeight);
            setSliderButtonHeight();
        });

        this.slider = new SliderComponent(new Layout(this.width - 10 - 6, 10, 6, this.height - 40));

        addComponent(effectList);
        addComponent(slider);
        addButton(10, this.height - 5 - 20, 100, 20, "Edit", () -> displayGui(new SkillEffectChooseGui(this)));

        if (selectedEffects.isEmpty()) {
            displayGui(new SkillEffectChooseGui(this));
        }
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        super.drawBackground();
        if (effectList.hasSelected()) {
            renderFormTexture();
            this.formTexture.render(this.formLayout);
        }
    }

    @Override
    protected void listener(ListenerRegistry listener) {

    }

    private void renderFormTexture() {
        val effect = this.effectList.getSelected();
        val paramList = effect.getParamList();
        this.formTexture.startDrawTexture();
//        drawString();
        // TODO Abstract into components
        this.formTexture.endDrawTexture();
    }

    private void setSliderButtonHeight() {
        int height = this.formLayout.getHeight() * this.formLayout.getHeight();

        if (this.selectedEffects.size() > 0) {
            height /= this.formContentHeight;
        }

        if (height < 32) {
            height = 32;
        }

        if (height > this.formLayout.getHeight() - 8) {
            height = this.formLayout.getHeight() - 8;
        }
        this.slider.setButtonHeight(height);
    }
}
