package skillapi.client.gui;

import cpw.mods.fml.client.GuiScrollingList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.BaseGui;
import skillapi.api.gui.base.Layout;
import skillapi.skill.BaseSkillEffect;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Jun
 * @date 2020/10/27.
 */
@SideOnly(Side.CLIENT)
public final class SkillEffectListGui extends GuiScrollingList {
    private final BaseGui parent;
    private final ArrayList<Class<? extends BaseSkillEffect>> effects;

    private final ClickEvent clickEvent;
    private int selectedIndex = -1;

    public SkillEffectListGui(BaseGui parent, Layout config, Collection<Class<? extends BaseSkillEffect>> effects) {
        this(parent, config, effects, null);
    }

    public SkillEffectListGui(BaseGui parent, Layout config, Collection<Class<? extends BaseSkillEffect>> effects, ClickEvent clickEvent) {
        super(parent.mc, config.getWidth(), config.getHeight(), config.getY(), config.getBottom(), config.getX(), 25);
        this.parent = parent;
        this.effects = new ArrayList<>(effects);
        this.clickEvent = clickEvent;
    }

    public boolean add(Class<? extends BaseSkillEffect> item) {
        return this.effects.add(item);
    }

    public Class<? extends BaseSkillEffect> remove(int index) {
        if (this.effects.size() > index && index > -1) {
            return this.effects.remove(index);
        }
        return null;
    }

    public ArrayList<Class<? extends BaseSkillEffect>> getEffects() {
        return effects;
    }

    public boolean canRemoveSelected() {
        return this.effects.size() > selectedIndex && selectedIndex > -1;
    }

    public Class<? extends BaseSkillEffect> removeSelected() {
        return this.effects.remove(this.selectedIndex);
    }

    @Override
    protected int getSize() {
        return this.effects.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.selectedIndex = index;

        if (clickEvent != null) {
            clickEvent.click(index, doubleClick);
        }
    }

    @Override
    protected boolean isSelected(int index) {
        return this.selectedIndex == index;
    }

    @Override
    protected void drawBackground() {}

    public void overlay() {
        overlayBackground(0, this.top, this.left, this.left + this.listWidth + 30, 0xFF);
        overlayBackground(this.bottom, this.parent.height, this.left, this.left + this.listWidth + 30, 0xFF);
    }

    private void overlayBackground(int top, int bottom, int left, int right, int alpha) {
        Tessellator tessellator = Tessellator.instance;
        this.parent.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float factor = 32.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0x404040, alpha);
        tessellator.addVertexWithUV(left, bottom, 0.0D, 0.0D, (double) bottom / factor);
        tessellator.addVertexWithUV(right, bottom, 0.0D, (double) (this.listWidth + 30) / factor, (double) bottom / factor);
        tessellator.addVertexWithUV(right, top, 0.0D, (double) (this.listWidth + 30) / factor, (double) top / factor);
        tessellator.addVertexWithUV(left, top, 0.0D, 0.0D, (double) top / factor);
        tessellator.draw();
    }

    @Override
    protected void drawSlot(int listIndex, int boxRight, int boxTop, int var4, Tessellator var5) {
        final Class<? extends BaseSkillEffect> effect = this.effects.get(listIndex);
        final FontRenderer fontRender = this.parent.getFontRenderer();

        String name = effect.getSimpleName();
        final int nameWidth = fontRender.getStringWidth(name);
        if (nameWidth > listWidth) {
            name = fontRender.trimStringToWidth(name, listWidth - 10 - (fontRender.getCharWidth('.') * 3)) + "...";
        }
        fontRender.drawString(name, this.left + 3, boxTop + 2, 0xFFFFFF);
    }

    @FunctionalInterface
    interface ClickEvent {
        /**
         * Triggered when the item is clicked
         *
         * @param index       Clicked item index
         * @param doubleClick Whether the item was double clicked
         */
        void click(int index, boolean doubleClick);
    }
}
