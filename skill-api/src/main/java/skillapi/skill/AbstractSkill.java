package skillapi.skill;

import skillapi.common.Translation;

/**
 * @author Jun
 */
public abstract class AbstractSkill extends AbstractSkillEffect {
    protected String name;

    protected int mana;
    protected long cooldown;
    protected int charge;

    public final int getMana() {
        return mana;
    }

    public final long getCooldown() {
        return cooldown;
    }

    public final int getCharge() {
        return charge;
    }

    @Override
    public String getUnlocalizedName() {
        return this.name;
    }

    public String getLocalizedName() {
        return Translation.format(this.name);
    }

    public String getDescription() {
        return Translation.format(this.name + ".description");
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
