package skillapi.skill;

import skillapi.common.SkillRuntimeException;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public abstract class BaseStaticSkill extends BaseSkill {
    private final static String DEFAULT_REGEX_NAME = "^[a-zA-z]+$";

    public BaseStaticSkill() {
        this.init(new StaticSkillConfig(this));
        if (!this.name.matches(DEFAULT_REGEX_NAME)) {
            throw new SkillRuntimeException("Invalid skill name: %s", this.name);
        }
    }

    /**
     * Initialize various parameters of static skills
     *
     * @param config Configuration
     */
    protected abstract void init(StaticSkillConfig config);
}
