package skillapi.api.gui.base;

import lombok.Builder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jun
 * @date 2020/11/3.
 */
public abstract class BaseGui extends GuiScreen {
    private final List<BaseComponent> components = new LinkedList<>();
    private final List<Button> buttons = new ArrayList<>();

    /**
     * Init gui
     */
    protected abstract void init();

    /**
     * Render function
     *
     * @param mouseX       Mouse x axis
     * @param mouseY       Mouse y axis
     * @param partialTicks Time tick
     */
    protected abstract void render(int mouseX, int mouseY, float partialTicks);

    @Override
    public final void initGui() {
        components.clear();
        buttons.clear();

        init();
    }

    @Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        render(mouseX, mouseY, partialTicks);

        renderComponent(mouseX, mouseY, partialTicks);
        renderButton(mouseX, mouseY);

        // Must be placed last, otherwise it will affect the display
        mouseCheck(mouseX, mouseY);
    }

    private void mouseCheck(int mouseX, int mouseY) {
        if (Mouse.isButtonDown(0)) {
            if (!GuiConst.isMouseLeftButtonPressed) {
                GuiConst.isMouseLeftButtonPressed = true;
                for (BaseComponent component : this.components) {
                    if (component.mousePressed(mouseX, mouseY)) {
                        break;
                    }
                }
            }
        } else {
            if (GuiConst.isMouseLeftButtonPressed) {
                GuiConst.isMouseLeftButtonPressed = false;
                for (BaseComponent component : this.components) {
                    if (component.mouseReleased(mouseX, mouseY)) {
                        break;
                    }
                }
            }
        }
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
    protected Button addButton(int x, int y, int width, int height, String text, FunctionalEvent event) {
        final Button button = new Button(this.buttons.size(), x, y, width, height, translate(text), event);
        this.buttons.add(button);
        return button;
    }

    protected <T extends BaseComponent> T addComponent(T component) {
        components.add(component);
        return component;
    }

    private String translate(String text) {
        return text.startsWith("$") ? I18n.format(text.substring(1)) : text;
    }

    private void renderButton(int mouseX, int mouseY) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        for (Button btn : this.buttons) {
            btn.guiButton.drawButton(this.mc, mouseX, mouseY);
        }
    }

    private void renderComponent(int mouseX, int mouseY, float partialTicks) {
        for (BaseComponent component : components) {
            component.render(mouseX, mouseY, partialTicks);
        }
    }

    protected void displayGui(BaseGui gui) {
        mc.displayGuiScreen(gui);
    }

    protected void drawCenteredString(String text, int centerX, int centerY, int color) {
        this.drawCenteredString(this.fontRendererObj, translate(text), centerX, centerY, color);
    }

    protected void drawString(String text, int x, int y, int color) {
        this.drawString(this.fontRendererObj, translate(text), x, y, color);
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int x, int y, int button) {
        // Left mouse button down
        if (button == 0) {
            for (Button btn : this.buttons) {
                if (btn.guiButton.mousePressed(this.mc, x, y)) {
                    btn.guiButton.func_146113_a(this.mc.getSoundHandler());
                    btn.doEvent();

                    // Only execute a mouse click event
                    return;
                }
            }
        }

    }

    /**
     * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call
     * Container.validate()
     */
    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.fontRendererObj = mc.fontRenderer;
        this.width = width;
        this.height = height;
        GuiConst.reload();

        this.buttons.clear();
        this.initGui();
    }

    protected static class Button {
        private final GuiButton guiButton;
        private final FunctionalEvent event;

        @Builder
        Button(int id, int x, int y, int width, int height, String text, FunctionalEvent event) {
            this.guiButton = new GuiButton(id, x, y, width, height, text);
            this.event = event;
        }

        public void doEvent() {
            if (event != null) {
                event.apply();
            }
        }

        public void setEnabled(boolean enabled) {
            this.guiButton.enabled = enabled;
        }

        public boolean getEnabled() {
            return this.guiButton.enabled;
        }
    }

    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }

    public int getScaleFactor() {
        return GuiConst.getScaleFactor();
    }

    @FunctionalInterface
    public interface FunctionalEvent {
        /**
         * The event to be executed
         */
        void apply();
    }
}
