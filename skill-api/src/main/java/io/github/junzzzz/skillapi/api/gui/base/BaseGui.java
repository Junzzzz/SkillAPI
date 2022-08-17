package io.github.junzzzz.skillapi.api.gui.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.junzzzz.skillapi.api.gui.base.listener.*;
import io.github.junzzzz.skillapi.api.gui.component.ButtonComponent;
import io.github.junzzzz.skillapi.api.util.function.EventFunction;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * @author Jun
 * @date 2020/11/3.
 */
@SideOnly(Side.CLIENT)
public abstract class BaseGui extends GenericGui {
    protected int width;
    protected int height;

    /**
     * Init gui
     */
    protected abstract void init();

    protected final void initGui() {
        components.clear();
        listenerRegistry.clear();
        init();
        // Null for gui
        listenerRegistry.onComponent(null);
        this.listener(listenerRegistry);
    }

    private void initRenderer() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        initRenderer();
        render(mouseX, mouseY, partialTicks);

        renderComponent(mouseX, mouseY, partialTicks);
    }

    protected void keyTyped(char eventCharacter, int eventKey) {
        // ESC quit
        if (eventKey == Keyboard.KEY_ESCAPE) {
            GuiApi.closeGui();
            return;
        }
        listenerRegistry.call(KeyTypedListener.class, l -> l.keyTyped(eventCharacter, eventKey));
    }

    protected void updateScreen() {
        listenerRegistry.call(UpdateScreenListener.class, UpdateScreenListener::onUpdate);
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
    protected final ButtonComponent addButton(int x, int y, int width, int height, String text, EventFunction event) {
        final ButtonComponent button = new ButtonComponent(new Layout(x, y, width, height), translate(text), event);
        return addComponent(button);
    }

    private void renderComponent(int mouseX, int mouseY, float partialTicks) {
        for (BaseComponent component : components) {
            initRenderer();
            component.render(mouseX, mouseY, partialTicks);
        }
    }

    protected final void displayGui(BaseGui gui) {
        GuiApi.displayGui(gui);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        // Left mouse button down
        if (button == MouseButton.LEFT.button) {
            loseFocus(mouseX, mouseY);
            listenerRegistry.call(MousePressedListener.class, (c, l) -> {
                if (c != null) {
                    final GenericGui parent = c.parent;
                    if (c.layout.isIn(mouseX, mouseY)) {
                        if (parent.focusComponent != c) {
                            parent.focusComponent = c;
                            ListenerRegistry.call(c, FocusChangedListener.class, fl -> fl.onFocus(true));
                        }
                        l.onPressed(mouseX, mouseY);
                    } else if (parent.focusComponent == c) {
                        parent.focusComponent = null;
                        ListenerRegistry.call(c, FocusChangedListener.class, fl -> fl.onFocus(false));
                    }
                } else {
                    l.onPressed(mouseX, mouseY);
                }
            });
        }
    }

    protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
        if (which == MouseButton.LEFT.button) {
            loseFocus(mouseX, mouseY);
            listenerRegistry.call(MouseReleasedListener.class, (c, l) -> {
                if (c != null) {
                    if (c.layout.isIn(mouseX, mouseY)) {
                        if (c.parent.focusComponent != c) {
                            ListenerRegistry.call(c, FocusChangedListener.class, fl -> fl.onFocus(true));
                            c.parent.focusComponent = c;
                        }
                        l.onReleased(mouseX, mouseY);
                    }
                } else {
                    l.onReleased(mouseX, mouseY);
                }
            });
        }
    }

    protected void loseFocus(int mouseX, int mouseY) {
        listenerRegistry.call(FocusChangedListener.class, (c, l) -> {
            if (c != null) {
                // Gui component
                if (c.parent.focusComponent == c && !c.layout.isIn(mouseX, mouseY)) {
                    l.onFocus(false);
                    c.focusComponent = null;
                }
            } else {
                // Gui
                if (!Mouse.isInsideWindow()) {
                    l.onFocus(false);
                }
            }
        });
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
