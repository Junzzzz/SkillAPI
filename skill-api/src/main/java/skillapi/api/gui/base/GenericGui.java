package skillapi.api.gui.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jun
 * @date 2021/3/8.
 */
public abstract class GenericGui {
    protected GenericGui parent;
    protected final List<BaseComponent> components = new LinkedList<>();
    protected BaseComponent focusComponent;

    protected final ListenerRegistry listenerRegistry = new ListenerRegistry(this);

    protected final <T extends BaseComponent> T addComponent(T component) {
        component.parent = this;
        components.add(component);
        listenerRegistry.onComponent(component);
        component.listener(listenerRegistry);
        return component;
    }

    /**
     * Render function
     *
     * @param mouseX       Mouse x axis
     * @param mouseY       Mouse y axis
     * @param partialTicks Time tick
     */
    protected abstract void render(int mouseX, int mouseY, float partialTicks);

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
}
