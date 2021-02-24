package skillapi.api.gui.component;

import lombok.Builder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.BaseComponent;
import skillapi.api.gui.base.GuiBox;

/**
 * @author Jun
 * @date 2020/11/18.
 */
public class SliderComponent extends BaseComponent {
    private final GuiBox sliderButton;

    private boolean isDragging;
    private int moveDistanceMax;

    private int buttonInitialY;
    private int buttonClickPosY;
    private int buttonMinY;
    private int buttonMaxY;

    @Builder
    public SliderComponent(GuiBox sliderBox) {
        super(sliderBox);
        this.sliderButton = new GuiBox(sliderBox.getX(), sliderBox.getY(), sliderBox.getWidth(), sliderBox.getHeight());

        this.isDragging = false;
        this.buttonClickPosY = -1;
    }

    public int getButtonHeight() {
        return this.sliderButton.getHeight();
    }

    public void setButtonHeight(int height) {
        this.sliderButton.setHeight(height);

        this.buttonMinY = this.guiBox.getY();
        this.buttonMaxY = this.guiBox.getBottom() - this.sliderButton.getHeight();

        // The maximum distance the button can move
        this.moveDistanceMax = this.guiBox.getHeight() - this.sliderButton.getHeight();
    }

    /**
     * Get the moving ratio of the slider
     *
     * @return Value of ratio
     */
    public double getRatio() {
        if (this.guiBox.getHeight() == this.sliderButton.getHeight()) {
            return 1.0;
        }
        return 1.0 * (this.sliderButton.getY() - this.guiBox.getY()) / (this.guiBox.getHeight() - this.sliderButton.getHeight());
    }

    @Override
    protected boolean mousePressed(int mouseX, int mouseY) {
        if (sliderButton.isInBox(mouseX, mouseY)) {
            // Click slider button -> Start dragging
            isDragging = true;
            buttonClickPosY = mouseY;
            buttonInitialY = this.sliderButton.getY();
            return true;
        }
        return false;
    }

    @Override
    protected boolean mouseReleased(int mouseX, int mouseY) {
        if (isDragging) {
            isDragging = false;
        }
        return true;
    }

    @Override
    protected void render(int mouseX, int mouseY, float partialTicks) {
        if (isDragging && moveDistanceMax > 0) {
            // Slider button exists
            int y = mouseY - buttonClickPosY + buttonInitialY;
            if (y > this.buttonMaxY) {
                y = this.buttonMaxY;
            } else if (y < this.buttonMinY) {
                y = this.buttonMinY;
            }
            // Dragging slider button
            this.sliderButton.setY(y);
        }
        // Render button
        renderSlider();
    }

    private void renderSlider() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);

        drawRect(guiBox.getX(), guiBox.getY(), guiBox.getRight(), guiBox.getBottom(), 0x000000);
        drawRect(sliderButton.getX(), sliderButton.getY(), sliderButton.getRight(), sliderButton.getBottom(), 0x808080);
        drawRect(sliderButton.getX(), sliderButton.getY(), sliderButton.getRight() - 1, sliderButton.getBottom() - 1, 0xC0C0C0);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawRect(int x1, int y1, int x2, int y2, int color) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        GL11.glColor3f(r, g, b);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y2, 0.0D);
        tessellator.addVertex(x2, y2, 0.0D);
        tessellator.addVertex(x2, y1, 0.0D);
        tessellator.addVertex(x1, y1, 0.0D);
        tessellator.draw();
    }
}
