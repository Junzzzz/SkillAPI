package io.github.junzzzz.skillapi.client.gui;

import io.github.junzzzz.skillapi.api.gui.base.*;
import io.github.junzzzz.skillapi.api.gui.base.listener.ComponentUpdateListener;
import io.github.junzzzz.skillapi.api.gui.base.listener.MousePressedListener;
import io.github.junzzzz.skillapi.api.gui.component.ButtonComponent;
import io.github.junzzzz.skillapi.client.gui.component.SkillIconListComponent;
import io.github.junzzzz.skillapi.common.SkillLog;
import io.github.junzzzz.skillapi.skill.AbstractSkill;
import io.github.junzzzz.skillapi.skill.SkillIcon;
import io.github.junzzzz.skillapi.skill.Skills;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Jun
 */
public class SkillIconChooseGui extends BaseGui {
    // Selected
    private final ResourceLocation chosen;
    private final Consumer<ResourceLocation> callback;

    private List<ResourceLocation> resources;
    private SkillIconListComponent list;

    public SkillIconChooseGui(Consumer<ResourceLocation> callback, ResourceLocation current) {
        loadIconList();
        this.callback = callback;
        this.chosen = current;
    }

    private void loadIconList() {
        this.resources = new ArrayList<>();

        for (String modId : Skills.getModIds()) {
            String prefix = "assets/" + modId + "/" + AbstractSkill.RESOURCE_ICON_PREFIX;
            int prefixLength = prefix.length();

            URL resourceURL = this.getClass().getResource("/" + prefix);
            if (resourceURL == null) {
                continue;
            }
            try {
                URLConnection connection = resourceURL.openConnection();
                if (!(connection instanceof JarURLConnection)) {
                    continue;
                }
                JarFile jarFile = ((JarURLConnection) connection).getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (!name.startsWith(prefix) || !name.endsWith(".png")) {
                        continue;
                    }
                    ResourceLocation resource = new ResourceLocation(modId, AbstractSkill.RESOURCE_ICON_PREFIX + entry.getName().substring(prefixLength));
                    resources.add(resource);
                }
            } catch (IOException e) {
                SkillLog.error(e, "Read mod %s icons failed.", modId);
            }
        }
    }

    @Override
    protected void init() {
        ButtonComponent finishButton = addButton(width / 2 - 40, height - 30, 80, 20, "$gui.done", () -> {
            callback.accept(this.list.getSelected());
        });

        Layout listBox = new Layout(10, 30, width - 10 - 10, height - 30 - 40);
        this.list = new SkillIconListComponent(listBox, this.resources, this.chosen);
        this.list.setAssociatedButton(finishButton);
        finishButton.setEnable(this.list.hasSelected());

        addComponent(this.list);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground();
    }

    @Override
    protected void listener(ListenerRegistry listener) {

    }

    public static class SkillIconChooseButtonComponent extends BaseComponent {
        @Setter
        private ResourceLocation currentIcon;

        public SkillIconChooseButtonComponent(int x, int y, ResourceLocation icon) {
            super(new Layout(x, y, 16, 16));
            this.currentIcon = icon;
        }

        @Override
        public void render(int mouseX, int mouseY, float partialTicks) {
            if (currentIcon != null) {
                RenderUtils.bindTexture(currentIcon);
                RenderUtils.drawTexturedModalRect(layout.getX(), layout.getY(), 0, 0, 16, 16, 0.0625D);
            } else {
                RenderUtils.drawCenteredString("+", layout.getCenterX(), layout.getY() + 6, Color.WHITE.getRGB());
            }
            if (!Mouse.isButtonDown(MouseButton.LEFT.button) && layout.isIn(mouseX, mouseY)) {
                RenderUtils.drawGradientRect(layout.getX(), layout.getY(), layout.getRight(), layout.getBottom(), 0x80BCC4D0, 0x80293445);
            }
        }

        @Override
        protected void listener(ListenerRegistry listener) {
            MousePressedListener l = (x, y) -> {
                onClick();
            };
            listener.on(l);
        }

        private void onClick() {
            BaseGui parent = GuiApi.getCurrentGui();

            Consumer<ResourceLocation> callback = icon -> {
                if (parent instanceof SkillEditGui) {
                    SkillEditGui editGui = (SkillEditGui) parent;
                    editGui.skillBuilder.setIcon(icon);
                    // TODO 会丢失其他未保存内容... 不碍事先留着
                    editGui.saveSkill();
                }
                this.currentIcon = icon;
                GuiApi.displayGui(parent);
                callParent(ComponentUpdateListener.class, l -> l.onUpdate(this));
            };

            GuiApi.displayGui(new SkillIconChooseGui(callback, this.currentIcon));
        }

        public String getIconStr() {
            return SkillIcon.stringify(this.currentIcon);
        }
    }
}
