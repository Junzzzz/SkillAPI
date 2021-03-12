package skillapi.api.gui.component;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.*;
import skillapi.api.util.function.EventFunction;

/**
 * TODO SOUND
 * Remake the original GuiButton component.
 * Button height cannot exceed 20, width cannot exceed 200.
 *
 * @author Jun
 * @date 2021/3/8.
 */
public class ButtonComponent extends BaseComponent {
    @Getter
    @Setter
    private boolean enable;
    private String text;
    private final EventFunction clickEvent;

    private final CachedTexture disableTexture;
    private final CachedTexture normalTexture;
    private final CachedTexture focusTexture;

    public ButtonComponent(Layout layout, String text, EventFunction event) {
        super(layout);
        this.enable = true;
        this.text = text;
        this.clickEvent = event;
        this.disableTexture = new CachedTexture(layout.getWidth(), layout.getHeight(), true);
        this.normalTexture = new CachedTexture(layout.getWidth(), layout.getHeight(), true);
        this.focusTexture = new CachedTexture(layout.getWidth(), layout.getHeight(), true);
        init();
    }

    private void init() {
        this.disableTexture.startDrawTexture();
        drawButton(this.text, 0, 0xA0A0A0);
        this.disableTexture.endDrawTexture();

        this.normalTexture.startDrawTexture();
        drawButton(this.text, 1, 0xE0E0E0);
        this.normalTexture.endDrawTexture();

        this.focusTexture.startDrawTexture();
        drawButton(this.text, 2, 0xFFFFA0);
        this.focusTexture.endDrawTexture();
    }

    public void setText(String text) {
        this.text = text;
        this.disableTexture.clear();
        this.normalTexture.clear();
        this.focusTexture.clear();
        init();
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        if (!visible) {
            return;
        }
        if (this.enable) {
            if (this.layout.isInBox(mouseX, mouseY)) {
                this.focusTexture.render(layout);
            } else {
                this.normalTexture.render(layout);
            }
        } else {
            this.disableTexture.render(layout);
        }
    }

    private void drawButton(String displayString, int order, int color) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiConst.BUTTON_TEXTURES);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // Start drawing from (0, 0)
        RenderUtils.drawTexturedModalRect(0, 0, 0, 46 + order * 20, layout.getWidth() / 2, layout.getHeight());
        RenderUtils.drawTexturedModalRect(layout.getWidth() / 2, 0, 200 - layout.getWidth() / 2, 46 + order * 20, layout.getWidth() / 2, layout.getHeight());

        RenderUtils.drawCenteredString(displayString, layout.getWidth() / 2, (layout.getHeight() - 8) / 2, color);
    }

    @Override
    protected boolean mousePressed(int mouseX, int mouseY, MouseButton button) {
        if (layout.isInBox(mouseX, mouseY)) {
            getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
            return true;
        }
        return false;
    }

    @Override
    protected boolean mouseReleased(int mouseX, int mouseY) {
        if (this.enable && layout.isInBox(mouseX, mouseY) && this.clickEvent != null) {
            this.clickEvent.apply();
            return true;
        }
        return false;
    }
}
