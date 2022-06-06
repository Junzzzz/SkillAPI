package skillapi.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import skillapi.api.gui.base.RenderUtils;

/**
 * @author Jun
 */
public class SkillPotion extends Potion {
    private ResourceLocation resource;

    protected SkillPotion(int id, String name, boolean isBadEffect, int liquidColor) {
        super(id, isBadEffect, liquidColor);
        setPotionName("skill.potion." + name);
    }

    protected void setIconResource(ResourceLocation resourceLocation) {
        this.resource = resourceLocation;
    }

    @Override
    public Potion setPotionName(String p_76390_1_) {
        return super.setPotionName(p_76390_1_);
    }

    protected Potion setIconIndex(int index) {
        return setIconIndex(index % 8, index / 8);
    }

    /**
     * From 0
     */
    @Override
    protected Potion setIconIndex(int row, int column) {
        return super.setIconIndex(column, row);
    }

    @Override
    public int getStatusIconIndex() {
        if (resource != null) {
            RenderUtils.bindTexture(resource);
        }
        return super.getStatusIconIndex();
    }
}