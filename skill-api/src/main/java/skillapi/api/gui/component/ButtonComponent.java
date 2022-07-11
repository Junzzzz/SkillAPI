package skillapi.api.gui.component;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.*;
import skillapi.api.gui.base.listener.ComponentUpdateListener;
import skillapi.api.gui.base.listener.FocusChangedListener;
import skillapi.api.gui.base.listener.MousePressedListener;
import skillapi.api.gui.base.listener.MouseReleasedListener;
import skillapi.api.util.function.EventFunction;

import java.awt.*;

/**
 * Remake the original GuiButton component.
 * Button height cannot exceed 20, width cannot exceed 200.
 *
 * @author Jun
 */
public class ButtonComponent extends BaseComponent {
    private static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

    private boolean _buttonClick;

    @Getter
    @Setter
    private boolean enable;
    private String text;
    private int color;
    private final EventFunction clickEvent;

    private final CachedTexture disableTexture;
    private final CachedTexture normalTexture;
    private final CachedTexture focusTexture;

    public ButtonComponent(Layout layout, String text, EventFunction event) {
        super(layout);
        this.enable = true;
        this.text = text;
        this.color = 0xE0E0E0;
        this.clickEvent = event;
        this.disableTexture = new CachedTexture(layout.getWidth(), layout.getHeight());
        this.normalTexture = new CachedTexture(layout.getWidth(), layout.getHeight());
        this.focusTexture = new CachedTexture(layout.getWidth(), layout.getHeight());
        init();
    }

    private void init() {
        this.disableTexture.startDrawTexture();

        drawButton(this.text, 0, 0xA0A0A0);
        this.disableTexture.endDrawTexture();

        this.normalTexture.startDrawTexture();
        drawButton(this.text, 1, color);
        this.normalTexture.endDrawTexture();

        this.focusTexture.startDrawTexture();
        drawButton(this.text, 2, new Color(color).brighter().getRGB());
        this.focusTexture.endDrawTexture();

        callParent(ComponentUpdateListener.class, listener -> listener.onUpdate(this));
    }

//    private int colorMultiply(int colorA, int colorB) {
//        colorA = 0xff000000 | colorA;
//        colorB = 0xff000000 | colorB;
//
//        int b = (colorA & 0xFF + colorB & 0xFF) >>> 1;
//        int g = ((colorA >> 8) & 0xFF + (colorB >> 8) & 0xFF) >>> 1;
//        int r = ((colorA >> 16) & 0xFF + (colorB >> 16) & 0xFF) >>> 1;
//        int a = ((colorA >> 24) & 0xFF + (colorB >> 24) & 0xFF) >>> 1;
//        return a << 24 | r << 16 | g << 8 | b;
//    }

    public void setText(String text) {
        this.text = text;
        this.disableTexture.clear();
        this.normalTexture.clear();
        this.focusTexture.clear();
        init();
    }

    public void setColor(int color) {
        this.color = color;

        this.disableTexture.clear();
        this.normalTexture.clear();
        this.focusTexture.clear();
        init();
    }

    public void setTextAndColor(String text, int color) {
        this.text = text;
        this.color = color;
        this.disableTexture.clear();
        this.normalTexture.clear();
        this.focusTexture.clear();
        init();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (!visible) {
            return;
        }
        if (this.enable) {
            if (this.layout.isIn(mouseX, mouseY)) {
                this.focusTexture.render(layout);
            } else {
                this.normalTexture.render(layout);
            }
        } else {
            this.disableTexture.render(layout);
        }
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        MousePressedListener pressed = (x, y) -> {
            this._buttonClick = true;
            getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
        };
        MouseReleasedListener released = (x, y) -> {
            if (this.enable && this.clickEvent != null && this._buttonClick) {
                this.clickEvent.apply();
            }
            this._buttonClick = false;
        };
        FocusChangedListener focus = f -> {
            if (!f) {
                this._buttonClick = false;
            }
        };

        listener.on(pressed, released);
    }

    private void drawButton(String displayString, int order, int color) {
        getTextureManager().bindTexture(BUTTON_TEXTURES);
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
}
