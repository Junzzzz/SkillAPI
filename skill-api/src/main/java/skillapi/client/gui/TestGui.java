package skillapi.client.gui;

import lombok.val;
import lombok.var;
import net.minecraft.client.gui.FontRenderer;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.api.gui.component.impl.ScrollingListComponent;
import skillapi.client.CachedTexture;

import java.util.ArrayList;

/**
 * @author Jun
 * @date 2020/11/22.
 */
public class TestGui extends BaseGui {
    private CachedTexture test;

    @Override
    protected void init() {
//        test = new CachedTexture(this.width, this.height, false);
//
//        test.startDrawTexture();
//        drawGradientRect(5, 5, this.width - 10, this.height - 10, 0xC0101010, 0xD0101010);
//
//        this.getFontRenderer().drawString("Test Frame Buffer Render", 5, 5, 0xFFFFFF);
//        this.getFontRenderer().drawString("Test Frame Buffer Render2", 5, 20, 0xFFFFFF);
//
//        test.endDrawTexture();
//        final SliderComponent slider = SliderComponent.builder()
//                .sliderBox(GuiBox.builder().x(this.width / 2).y(0).width(10).height(this.height).build())
//                .build();
//        slider.setButtonHeight(this.height / 2);
//
//        final SliderComponent slider2 = SliderComponent.builder()
//                .sliderBox(GuiBox.builder().x(0).y(0).width(10).height(this.height).build())
//                .build();
//        slider2.setButtonHeight(this.height / 2);
//        addComponent(slider);
//        addComponent(slider2);
        final var test = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            test.add(i);
        }
        final int listWidth = this.width / 2 - 6 - 2;
        val list = new ScrollingListComponent<>(
                Layout.builder().x(20).y(20).width(this.width / 2).height(this.height - 40).build(),
                30, test,
                (data, x, y) -> {
                    final FontRenderer fontRender = this.getFontRenderer();

                    String name = data.toString();
                    final int nameWidth = fontRender.getStringWidth(name);
                    if (nameWidth > listWidth) {
                        name = fontRender.trimStringToWidth(name, listWidth - 10 - (fontRender.getCharWidth('.') * 3)) + "...";
                    }
                    fontRender.drawString(name, x + 3, y + 2, 0xFFFFFF);
                }
        );
        addComponent(list);
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
//        test.render(0, 0, 0, 0, this.width, this.height);
//        renderFramebuffer(this.width, this.height);
//        this.getFontRenderer().drawString("Test Frame Buffer Render2", 50, 50, 0xFFFFFF);

    }
}
