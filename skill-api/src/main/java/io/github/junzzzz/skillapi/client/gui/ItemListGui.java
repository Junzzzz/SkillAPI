package io.github.junzzzz.skillapi.client.gui;

import io.github.junzzzz.skillapi.Application;
import io.github.junzzzz.skillapi.api.gui.base.BaseGui;
import io.github.junzzzz.skillapi.api.gui.base.ListenerRegistry;
import io.github.junzzzz.skillapi.api.gui.base.RenderUtils;
import io.github.junzzzz.skillapi.api.gui.base.listener.MousePressedListener;
import io.github.junzzzz.skillapi.api.gui.component.ButtonComponent;
import io.github.junzzzz.skillapi.common.PageHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * @author Jun
 */
public abstract class ItemListGui<T> extends BaseGui {
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

    private String title;

    private ButtonComponent nextPageButton;
    private ButtonComponent prevPageButton;

    protected final PageHelper<T> page;

    protected int selectedLine = -1;

    public ItemListGui(PageHelper<T> page) {
        this.page = page;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void init() {
        this.guiPositionX = (this.width - SKILL_LIST_WIDTH) / 2;
        this.guiPositionY = (this.height - SKILL_LIST_HEIGHT - 25) / 2;

        this.skillListPositionX = this.guiPositionX + SKILL_LIST_ITEM_X;
        this.skillListPositionY = this.guiPositionY + SKILL_LIST_ITEM_Y;

        // Prev page button
        prevPageButton = addButton(this.guiPositionX, this.guiPositionY + SKILL_LIST_HEIGHT, 20, 20, "<",
                () -> {
                    this.page.prevPage();
                    checkPageStatus();
                }
        );
        // Next page button
        nextPageButton = addButton(this.guiPositionX + SKILL_LIST_WIDTH - 20, this.guiPositionY + SKILL_LIST_HEIGHT, 20, 20, ">",
                () -> {
                    this.page.nextPage();
                    checkPageStatus();
                }
        );
    }

    @Override
    protected void listener(ListenerRegistry listener) {
        MousePressedListener press = (x, y) -> {
            // Do not lose focus when pressing button
            if (!keepFocus(x, y)) {
                int mouseOver = getMouseOver(x, y);
                if (mouseOver < SKILL_LIST_SIZE && this.selectedLine != mouseOver) {
                    selectedLineChanged(mouseOver);
                    checkPageStatus();
                }
            }
        };
        listener.on(press);
    }

    protected boolean keepFocus(int mouseX, int mouseY) {
        return false;
    }

    protected void selectedLineChanged(int newLine) {
        if (newLine < this.page.getCurrentPage().size()) {
            this.selectedLine = newLine;
        }
    }

    protected void checkPageStatus() {
        this.nextPageButton.setEnable(this.page.hasNextPage());
        this.prevPageButton.setEnable(this.page.hasPrevPage());
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
            RenderUtils.drawTexturedModalRect(
                    this.skillListPositionX, this.skillListPositionY + i * SKILL_LIST_ITEM_HEIGHT,
                    0, SKILL_LIST_HEIGHT, SKILL_LIST_ITEM_WIDTH,
                    SKILL_LIST_ITEM_HEIGHT
            );
        }

        // Title
        if (title != null) {
            drawString(title, guiPositionX + 6, guiPositionY + 6, Color.YELLOW.getRGB(), true);
        }
    }

    protected abstract void drawItem(int mouseX, int mouseY);

    protected void drawList(int mouseX, int mouseY) {
        if (selectedLine != -1) {
            RenderUtils.drawTexturedModalRect(
                    this.skillListPositionX, skillListPositionY + selectedLine * SKILL_LIST_ITEM_HEIGHT,
                    SKILL_LIST_ITEM_WIDTH, SKILL_LIST_HEIGHT,
                    SKILL_LIST_ITEM_WIDTH, SKILL_LIST_ITEM_HEIGHT
            );
        }

        drawItem(mouseX, mouseY);
    }

    protected void drawMouseOver(int mouseX, int mouseY) {
        getTextureManager().bindTexture(SKILL_LIST_TEXTURES);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
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
