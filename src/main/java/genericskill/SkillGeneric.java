package genericskill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import skillapi.Skill;

/**
 * Generic abstract skill class with no duration, special effect nor
 * prerequisite, and a generic type. Has setters for name and texture for
 * chaining convenience
 */
public abstract class SkillGeneric extends Skill {
    public String name;
    public ResourceLocation texture;

    @Override
    public String getName() {
        return name;
    }

    public SkillGeneric setName(String name) {
        this.name = name;
        return this;
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
        return "Generic Skill";
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
