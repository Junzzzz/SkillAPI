package skillapi.newskill;

/**
 * @author Jun
 */
public abstract class AbstractSkill implements SkillEffect {
    public static final String REGEX_NAME = "^[a-zA-z]+$";

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

    public final String getName() {
        return this.name;
    }
}
