package skillapi.skill;

/**
 * @author Jun
 * @date 2021/4/5.
 */
public class StaticSkillConfig {
    private final BaseStaticSkill staticSkill;

    public StaticSkillConfig(BaseStaticSkill staticSkill) {
        this.staticSkill = staticSkill;
    }

    /**
     * Initialize the skill name (English only)
     */
    public StaticSkillConfig name(String name) {
        this.staticSkill.name = name;
        return this;
    }

    /**
     * Initialize the amount of mana consumed
     */
    public StaticSkillConfig mana(int mana) {
        this.staticSkill.mana = mana;
        return this;
    }

    /**
     * Initialize skill charge time
     */
    public StaticSkillConfig charge(int charge) {
        this.staticSkill.charge = charge;
        return this;
    }

    /**
     * Initialize skill cooldown
     */
    public StaticSkillConfig cooldown(int cooldown) {
        this.staticSkill.cooldown = cooldown;
        return this;
    }
}
