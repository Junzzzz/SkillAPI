package skillapi.client.gui.component;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import skillapi.api.gui.base.*;
import skillapi.api.gui.component.SliderComponent;
import skillapi.client.GuiKnownSkills;
import skillapi.common.Translation;
import skillapi.skill.AbstractSkill;
import skillapi.skill.Skills;

import java.util.List;

/**
 * @author Jun
 */
public class KnownSkillListComponent extends BaseComponent {
    private final SliderComponent slider;
    private final List<AbstractSkill> skillList;
    private final int slotHeight = 28;
    private int movableWindowHeight;
    private CachedTexture listTexture;

    public KnownSkillListComponent(Layout layout, Layout sliderLayout, List<AbstractSkill> skillList) {
        super(layout);
        this.skillList = skillList;

        this.slider = new SliderComponent(sliderLayout);
        this.slider.setButtonHeight(15);
        this.slider.setRenderer((c, b, x, y) -> {
            RenderUtils.bindTexture(GuiKnownSkills.GUI);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderUtils.drawTexturedModalRect(b.getX(), b.getY(), 206, 0, 12, 15);
        });
        addComponent(this.slider);

        refreshListTexture();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.slider.render(mouseX, mouseY, partialTicks);
        if (this.skillList.size() > 0) {
            renderList();
        }
        int mouseOverIndex = getMouseOverIndex(mouseX, mouseY);
        if (mouseOverIndex != -1) {
            renderSkillDescription(this.skillList.get(mouseOverIndex), mouseX, mouseY);
        }
    }

    private void renderList() {
        int y = (int) (slider.getRatio() * this.movableWindowHeight);
        listTexture.render(layout.getX(), layout.getY(), 0, y, listTexture.getWidth(), layout.getHeight());
    }

    private void refreshListTexture() {
        CachedTexture tmp = listTexture;
        this.listTexture = createListTexture();

        // clear
        if (tmp != null) {
            tmp.delete();
        }
    }

    private void renderSkillDescription(AbstractSkill skill, int x, int y) {
        // Offset
        x += 9;
        FontRenderer fontRenderer = getFontRenderer();
        String name = skill.getLocalizedName();
        String[] desc = skill.getDescription().split("\\\\n");
        boolean emptyDesc = true;

        int width = fontRenderer.getStringWidth(name) + 10;
        int height = 20 + 9 * Math.max(desc.length, 1);

        for (String s : desc) {
            if (emptyDesc && !s.isEmpty()) {
                emptyDesc = false;
            }
            width = Math.max(fontRenderer.getStringWidth(s) + 10, width);
        }

        if (emptyDesc) {
            width = Math.max(fontRenderer.getStringWidth(Translation.format("skill.constant.noDescription") + 10), width);
        }

        RenderUtils.drawRect(GL11.GL_POLYGON, x - 1, y - 1, x + 1 + width, y + 1 + height, 0xCC000000);
        RenderUtils.drawRect(GL11.GL_QUADS, x, y, x + width, y + height, 0xCC001F5F);
        GL11.glEnable(GL11.GL_BLEND);

        // Skill name
        fontRenderer.drawStringWithShadow(name, x + 5, y + 5, 0xE0BC38);

        // Description lines
        if (!emptyDesc) {
            for (int i = 0; i < desc.length; i++) {
                fontRenderer.drawString(desc[i], x + 5, y + 15 + (9 * i), 0x8FA8FF);
            }
        } else {
            fontRenderer.drawString(Translation.format("skill.constant.noDescription"), x + 5, y + 15, 0x8FA8FF);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    public int getMouseOverIndex(int mouseX, int mouseY) {
        if (this.layout.isIn(mouseX, mouseY)) {
            int index = ((int) (slider.getRatio() * movableWindowHeight) + mouseY - layout.getY()) / slotHeight;
            if (index < this.skillList.size()) {
                return index;
            }
        }
        return -1;
    }

    public int getOffsetX(int mouseX) {
        return this.layout.getX() + 4 - mouseX;
    }

    public int getOffsetY(int index, int mouseY) {
        return (int) ((index * slotHeight) + 6 - (slider.getRatio() * movableWindowHeight + mouseY - layout.getY()));
    }

    private CachedTexture createListTexture() {
        this.movableWindowHeight = Math.max(getContentHeight() - this.layout.getHeight(), 0);
        this.slider.setMovable(this.movableWindowHeight > 0);

        CachedTexture cachedTexture = new CachedTexture(this.layout.getWidth(), getContentHeight());
        cachedTexture.startDrawTexture();
        for (int i = 0; i < this.skillList.size(); i++) {
            renderSlot(this.skillList.get(i), i * slotHeight);
        }
        cachedTexture.endDrawTexture();
        return cachedTexture;
    }

    private void renderSlot(AbstractSkill skill, int y) {
        String skillName = Skills.getLocalizedName(skill);
        drawString(skillName, 24, y + 4, 0xFFFFFF);

        ResourceLocation iconResource = skill.getIconResource();
        if (iconResource != null) {
            RenderUtils.bindTexture(iconResource);
            RenderUtils.drawTexturedModalRect(4, y + 6, 0, 0, 16, 16, 0.0625D);
        } else {
            String first = skillName.substring(0, 1);
            drawCenteredString(first, 4 + 8, y + 9, 0xFFFFFF);
        }
    }

    public AbstractSkill getSkill(int index) {
        return this.skillList.get(index);
    }

    @Override
    protected void listener(ListenerRegistry listener) {

    }

    private int getContentHeight() {
        return this.slotHeight * this.skillList.size();
    }

}
