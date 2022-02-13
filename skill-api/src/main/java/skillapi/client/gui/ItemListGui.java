package skillapi.client.gui;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import skillapi.Application;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.RenderUtils;
import skillapi.api.gui.base.listener.MousePressedListener;

/**
 * @author Jun
 */
public abstract class ItemListGui extends BaseGui {
    public static final ResourceLocation SKILL_LIST_TEXTURES = new ResourceLocation(Application.MOD_ID, "textures/gui/skill-list.png");

    public static final int SKILL_LIST_WIDTH = 121;
    public static final int SKILL_LIST_HEIGHT = 177;

    public static final int SKILL_LIST_ITEM_X = 6;
    public static final int SKILL_LIST_ITEM_Y = 18;

    public static final int SKILL_LIST_SIZE = 7;
    public static final int SKILL_LIST_ITEM_WIDTH = 108;
    public static final int SKILL_LIST_ITEM_HEIGHT = 19;
    public static final int SKILL_LIST_ITEM_HEIGHT_ALL = 19 * SKILL_LIST_SIZE;

    protected int guiPositionX;
    protected int guiPositionY;

    protected int skillListPositionX;
    protected int skillListPositionY;

    protected int selectedLine = -1;

    @Override
    protected void init() {
        this.guiPositionX = (this.width - SKILL_LIST_WIDTH) / 2;
        this.guiPositionY = (this.height - SKILL_LIST_HEIGHT - 25) / 2;

        this.skillListPositionX = this.guiPositionX + SKILL_LIST_ITEM_X;
        this.skillListPositionY = this.guiPositionY + SKILL_LIST_ITEM_Y;
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        MousePressedListener press = (x, y) -> {
            // Do not lose focus when pressing button
            if (!keepFocus(x, y)) {
                this.selectedLine = getMouseOver(x, y);
                if (this.selectedLine >= SKILL_LIST_SIZE) {
                    this.selectedLine = -1;
                }
            }
        };
        listener.on(press);
    }

    protected boolean keepFocus(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        GL11.glEnable(GL11.GL_BLEND);
        super.drawDefaultBackground();

        drawBackground();
        drawMouseOver(mouseX, mouseY);
        drawList(mouseX, mouseY);

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void drawBackground() {
        getTextureManager().bindTexture(SKILL_LIST_TEXTURES);
        RenderUtils.drawTexturedModalRect(guiPositionX, guiPositionY, 0, 0, SKILL_LIST_WIDTH, SKILL_LIST_HEIGHT);

        // Draw background
        for (int i = 0; i < SKILL_LIST_SIZE; i++) {
            RenderUtils.drawTexturedModalRect(this.skillListPositionX,
                    this.skillListPositionY + i * SKILL_LIST_ITEM_HEIGHT, 0, SKILL_LIST_HEIGHT, SKILL_LIST_ITEM_WIDTH,
                    SKILL_LIST_ITEM_HEIGHT);
        }
    }

    protected abstract void drawItem(int mouseX, int mouseY);

    protected void drawList(int mouseX, int mouseY) {
        if (selectedLine != -1) {
            RenderUtils.drawTexturedModalRect(this.skillListPositionX,
                    skillListPositionY + selectedLine * SKILL_LIST_ITEM_HEIGHT, SKILL_LIST_ITEM_WIDTH, SKILL_LIST_HEIGHT,
                    SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT);
        }

        drawItem(mouseX, mouseY);
    }

    protected void drawMouseOver(int mouseX, int mouseY) {
        final int mouseOver = getMouseOver(mouseX, mouseY);
        if (mouseOver != -1 && mouseOver < SKILL_LIST_SIZE) {
            RenderUtils.drawTexturedModalRect(this.skillListPositionX,
                    this.skillListPositionY + mouseOver * SKILL_LIST_ITEM_HEIGHT,
                    0, SKILL_LIST_HEIGHT + SKILL_LIST_ITEM_HEIGHT * 2,
                    SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT);
        }
    }

    /**
     * Get the line where the mouse is hovering
     *
     * @param mouseX Mouse x axis
     * @param mouseY Mouse y axis
     * @return Selected row. If not selected, return {@code -1}
     */
    protected int getMouseOver(int mouseX, int mouseY) {
        int x = mouseX - this.skillListPositionX;
        int y = mouseY - this.skillListPositionY;
        if (x > 0 && y > 0 && x < SKILL_LIST_ITEM_WIDTH && y < SKILL_LIST_ITEM_HEIGHT_ALL) {
            return y / SKILL_LIST_ITEM_HEIGHT;
        }
        return -1;
    }
}
