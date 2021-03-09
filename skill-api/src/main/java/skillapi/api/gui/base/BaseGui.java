package skillapi.api.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;
import skillapi.api.gui.component.ButtonComponent;
import skillapi.api.util.function.EventFunction;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jun
 * @date 2020/11/3.
 */
public abstract class BaseGui extends GuiScreen implements GenericGui {
    private final List<BaseComponent> components = new LinkedList<>();

    private boolean isMouseLeftButtonPressed;

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

        init();
    }

    @Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        render(mouseX, mouseY, partialTicks);

        renderComponent(mouseX, mouseY, partialTicks);

        // Must be placed last, otherwise it will affect the display
        mouseCheck(mouseX, mouseY);
    }

    private void mouseCheck(int mouseX, int mouseY) {
        if (Mouse.isButtonDown(0)) {
            if (!isMouseLeftButtonPressed) {
                isMouseLeftButtonPressed = true;
                for (BaseComponent component : this.components) {
                    if (component.mousePressed(mouseX, mouseY)) {
                        break;
                    }
                }
            }
        } else {
            if (isMouseLeftButtonPressed) {
                isMouseLeftButtonPressed = false;
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
    protected ButtonComponent addButton(int x, int y, int width, int height, String text, EventFunction event) {
        final ButtonComponent button = new ButtonComponent(new Layout(x, y, width, height), translate(text), event);
        this.components.add(button);
        return button;
    }

    protected <T extends BaseComponent> T addComponent(T component) {
        components.add(component);
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
        // TODO remake handleMouseInput
        // Left mouse button down
//        if (button == 0) {
//            for (Button btn : this.buttons) {
//                if (btn.guiButton.mousePressed(this.mc, x, y)) {
//                    btn.guiButton.func_146113_a(this.mc.getSoundHandler());
//                    btn.doEvent();
//
//                    // Only execute a mouse click event
//                    return;
//                }
//            }
//        }

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

        this.initGui();
    }

    @Override
    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }
}
