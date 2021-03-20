package skillapi.api.gui.base;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.listener.*;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.api.util.function.EventFunction;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jun
 * @date 2020/11/3.
 */
public abstract class BaseGui extends GenericGui {
    protected int width;
    protected int height;

    private final List<BaseComponent> components = new LinkedList<>();
    private final ListenerRegistry componentListener = new ListenerRegistry();
    private final ListenerRegistry guiListener = new ListenerRegistry();

    /**
     * Init gui
     */
    protected abstract void init();

    protected final void initGui() {
        components.clear();
        componentListener.clear();
        guiListener.clear();
        init();
        this.listener(guiListener);
    }

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        render(mouseX, mouseY, partialTicks);

        renderComponent(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char eventCharacter, int eventKey) {
        // ESC quit
        if (eventKey == Keyboard.KEY_ESCAPE) {
            GuiApi.closeGui();
            return;
        }
        guiListener.call(KeyTypedListener.class, l -> l.keyTyped(eventCharacter, eventKey));
        componentListener.call(KeyTypedListener.class, l -> l.keyTyped(eventCharacter, eventKey));
    }

    protected void updateScreen() {
        guiListener.call(UpdateScreenListener.class, UpdateScreenListener::onUpdate);
        componentListener.call(UpdateScreenListener.class, UpdateScreenListener::onUpdate);
    }

    /**
     * @param x      Screen x axis
     * @param y      Screen y axis
     * @param width  Button width
     * @param height Button height
     * @param text   Button text. If it starts with {@code $}, I18n will be automatically used
     * @param event  Button click event
     * @return Button
     */
    protected ButtonComponent addButton(int x, int y, int width, int height, String text, EventFunction event) {
        final ButtonComponent button = new ButtonComponent(new Layout(x, y, width, height), translate(text), event);
        return addComponent(button);
    }

    protected <T extends BaseComponent> T addComponent(T component) {
        components.add(component);
        component.listener(componentListener);
        return component;
    }

    private String translate(String text) {
        return text.startsWith("$") ? I18n.format(text.substring(1)) : text;
    }

    private void renderComponent(int mouseX, int mouseY, float partialTicks) {
        for (BaseComponent component : components) {
            component.render(mouseX, mouseY, partialTicks);
        }
    }

    protected void displayGui(BaseGui gui) {
        GuiApi.displayGui(gui);
    }

    protected void drawCenteredString(String text, int centerX, int centerY, int color) {
        final FontRenderer fontRenderer = getFontRenderer();
        text = translate(text);
        fontRenderer.drawString(text, centerX - fontRenderer.getStringWidth(text) / 2, centerY, color);
    }

    protected void drawString(String text, int x, int y, int color) {
        getFontRenderer().drawString(translate(text), x, y, color);
    }


    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        // Left mouse button down
        if (button == MouseButton.LEFT.button) {
            // TODO
            guiListener.call(MousePressedListener.class, l -> l.onPressed(mouseX, mouseY));
//            listener.call(FocusChangedListener.class, l -> l.onFocus(true));
//            listener.call(MousePressedListener.class, l -> l.onPressed(mouseX, mouseY));
        }
    }

    protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
        if (which == 0) {
            // TODO
            guiListener.call(MouseReleasedListener.class, l -> l.onReleased(mouseX, mouseY));
//            listener.call(FocusChangedListener.class, l -> l.onFocus(true));
//            listener.call(MouseReleasedListener.class, l -> l.onReleased(mouseX, mouseY));
        }
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
     */
    public void drawDefaultBackground() {
        this.drawWorldBackground();
    }

    public void drawWorldBackground() {
        if (getMinecraft().theWorld != null) {
            RenderUtils.drawGradientRect(0, 0, this.width, this.height, 0xC0101010, 0xD0101010);
        } else {
            this.drawBackground();
        }
    }

    /**
     * Draws the background
     */
    protected void drawBackground() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x404040);
        tessellator.addVertexWithUV(0.0D, this.height, 0.0D, 0.0D, this.height / f);
        tessellator.addVertexWithUV(this.width, this.height, 0.0D, this.width / f, this.height / f);
        tessellator.addVertexWithUV(this.width, 0.0D, 0.0D, this.width / f, 0);
        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.draw();
    }

    protected void onClose() {
        // TODO
    }
}
