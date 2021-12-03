package skillapi.skill;

import java.util.Objects;

/**
 * @author Jun
 */
public abstract class AbstractSkill extends AbstractSkillEffect {
    protected String name;

    protected int mana;
    protected int cooldown;
    protected int charge;

    public final int getMana() {
        return mana;
    }

    public final int getCooldown() {
        return cooldown;
    }

    public final int getCharge() {
        return charge;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractSkill that = (AbstractSkill) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}