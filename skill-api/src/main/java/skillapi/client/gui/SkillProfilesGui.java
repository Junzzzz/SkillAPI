package skillapi.client.gui;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import skillapi.Application;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.ListenerRegistry;
import skillapi.api.gui.base.RenderUtils;

/**
 * @author Jun
 */
public class SkillProfilesGui extends BaseGui {
    public static final ResourceLocation SKILL_LIST_TEXTURES = new ResourceLocation(Application.MOD_ID, "textures/gui/skill-list.png");

    public static final int SKILL_LIST_WIDTH = 121;
    public static final int SKILL_LIST_HEIGHT = 169;

    public static final int SKILL_LIST_ITEM_X = 6;
    public static final int SKILL_LIST_ITEM_Y = 10;

    public static final int SKILL_LIST_ITEM_WIDTH = 108;
    public static final int SKILL_LIST_ITEM_HEIGHT = 19;
    public static final int SKILL_LIST_ITEM_HEIGHT_ALL = 19 * 7;

    private int guiPositionX;
    private int guiPositionY;

    private int skillListPositionX;
    private int skillListPositionY;

    @Override
    protected void init() {
        this.guiPositionX = (this.width - SKILL_LIST_WIDTH) / 2;
        this.guiPositionY = (this.height - SKILL_LIST_HEIGHT - 25) / 2;

        this.skillListPositionX = this.guiPositionX + SKILL_LIST_ITEM_X;
        this.skillListPositionY = this.guiPositionY + SKILL_LIST_ITEM_Y;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        GL11.glEnable(GL11.GL_BLEND);
        super.drawDefaultBackground();

        drawBackground();
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void listener(ListenerRegistry listener) {

    }

    @Override
    protected void drawBackground() {
        getTextureManager().bindTexture(SKILL_LIST_TEXTURES);
        RenderUtils.drawTexturedModalRect(guiPositionX, guiPositionY, 0, 0, SKILL_LIST_WIDTH, SKILL_LIST_HEIGHT);
    }
}
