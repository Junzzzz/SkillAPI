package genericskill.skills;

import genericskill.utils.SkillUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import skillapi.Skill;

/**
 * Generic abstract skill class with no duration, special effect nor
 * prerequisite, and a generic type. Has setters for name and texture for
 * chaining convenience
 */
public abstract class SkillGeneric extends Skill {
    public String unlocalizedName;
    public String localizedName;
    public ResourceLocation texture;

    private String trimName;

    @Override
    public String getName() {
        return unlocalizedName;
    }

    public SkillGeneric setName(String name) {
        this.unlocalizedName = name;
        this.trimName = name.toLowerCase().replaceAll(" ", "");
        this.localizedName = SkillUtils.getSkillI18nName(trimName);
        return this;
    }

    @Override
    public String getLocalizedName() {
        return localizedName;
    }

    @Override
    public String getDescription() {
        return SkillUtils.getSkillDescription(trimName);
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    public SkillGeneric setTexture(String name) {
        this.texture = new ResourceLocation("genericskill", "textures/" + name + ".png");
        return this;
    }

    @Override
    public boolean canPlayerLearnSkill(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean canPlayerUseSkill(EntityPlayer player) {
        return true;
    }

    @Override
    public String getType() {
        return I18n.format("skill.type.name.genericsskill");
    }

    @Override
    public float getDuration(EntityPlayer player) {
        return 0;
    }

    @Override
    public void onSkillEnd(EntityPlayer player) {
    }

    @Override
    public void onTickWhileActive(EntityPlayer player) {
    }
}
