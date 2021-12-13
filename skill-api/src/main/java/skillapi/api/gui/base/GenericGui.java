package skillapi.api.gui.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jun
 * @date 2021/3/8.
 */
@SideOnly(Side.CLIENT)
public abstract class GenericGui {
    protected GenericGui parent;
    protected final List<BaseComponent> components = new LinkedList<>();
    protected BaseComponent focusComponent;

    protected final ListenerRegistry listenerRegistry = new ListenerRegistry(this);

    protected final <T extends BaseComponent> T addComponent(T component) {
        component.parent = this;
        this.components.add(component);
        this.listenerRegistry.onComponent(component);
        component.listener(this.listenerRegistry);
        return component;
    }

    protected final void addComponent(BaseComponent... component) {
        for (BaseComponent c : component) {
            c.parent = this;
            this.components.add(c);
            this.listenerRegistry.onComponent(c);
            c.listener(this.listenerRegistry);
        }
    }

    protected final void removeComponent(BaseComponent component) {
        this.components.remove(component);
        this.listenerRegistry.removeListener(component);
    }

    protected final void removeComponents(List<BaseComponent> components) {
        this.components.removeAll(components);
        this.listenerRegistry.removeListeners(components);
    }

    /**
     * Render function
     *
     * @param mouseX       Mouse x axis
     * @param mouseY       Mouse y axis
     * @param partialTicks Time tick
     */
    public abstract void render(int mouseX, int mouseY, float partialTicks);

    /**
     * Listener registration function
     *
     * @param listener Listener Registrar
     */
    protected abstract void listener(ListenerRegistry listener);

    /**
     * Get text renderer
     *
     * @return Renderer
     */
    protected final FontRenderer getFontRenderer() {
        return GuiApi.minecraft.fontRenderer;
    }

    /**
     * Get the game sound controller
     *
     * @return Sound handler
     */
    protected final SoundHandler getSoundHandler() {
        return GuiApi.minecraft.getSoundHandler();
    }

    /**
     * Get window zoom ratio
     *
     * @return The number of the ratio
     */
    protected final int getScaleFactor() {
        return GuiApi.scaleFactor;
    }

    /**
     * @return The minecraft!
     * @see net.minecraft.client.Minecraft
     */
    protected final Minecraft getMinecraft() {
        return GuiApi.minecraft;
    }

    /**
     * The RenderEngine instance used by Minecraft
     */
    protected final TextureManager getTextureManager() {
        return GuiApi.minecraft.renderEngine;
    }

    protected final void drawCenteredString(String text, int centerX, int y, int color) {
        final FontRenderer fontRenderer = getFontRenderer();
        text = translate(text);
        fontRenderer.drawString(text, centerX - fontRenderer.getStringWidth(text) / 2, y, color);
    }

    protected final void drawString(String text, int x, int y, int color) {
        getFontRenderer().drawString(translate(text), x, y, color);
    }

    protected String translate(String text) {
        return text.startsWith("$") ? I18n.format(text.substring(1)) : text;
    }
}
