package skillapi.api.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author Jun
 * @date 2021/3/15.
 */
public final class GuiApi extends GuiScreen {
    private BaseGui currentGui;

    public GuiApi(BaseGui currentGui) {
        this.currentGui = currentGui;
    }

    @Override
    public void initGui() {
        currentGui.mc = this.mc;
        currentGui.width = this.width;
        currentGui.height = this.height;
        this.currentGui.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.currentGui.drawScreen(mouseX, mouseY, partialTicks);
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

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        this.currentGui.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
        this.currentGui.mouseMovedOrUp(mouseX, mouseY, which);
    }

    private void switchGui(BaseGui gui) {
        this.currentGui.close();
        this.currentGui = gui;
        initGui();
    }

    public static void displayGui(BaseGui gui) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof GuiApi) {
            ((GuiApi) mc.currentScreen).switchGui(gui);
        } else {
            mc.displayGuiScreen(new GuiApi(gui));
        }
    }
}
