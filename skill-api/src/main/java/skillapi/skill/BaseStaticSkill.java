package skillapi.skill;

import skillapi.common.SkillRuntimeException;
import skillapi.utils.StringUtils;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public abstract class BaseStaticSkill extends BaseSkill {
    public BaseStaticSkill() {
        final String skillName = initName();
        if (!skillName.matches(StringUtils.DEFAULT_REGEX_NAME)) {
            throw new SkillRuntimeException("Invalid skill name: %s", skillName);
        }
        if (SkillHandler.containsSkill(skillName)) {
            throw new SkillRuntimeException("Duplicate skill name: %s", skillName);
        }
        super.setName(skillName);
        super.setMana(initMana());
        super.setCooldown(initCooldownTime());
        super.setCharge(initChargeTime());
    }

    /**
     * Initialize the skill name (English only)
     *
     * @return Attribute-skill name
     */
    protected abstract String initName();

    /**
     * Initialize the amount of mana consumed
     *
     * @return Attribute-mana value
     */
    protected abstract int initMana();

    /**
     * 初始化冷却时间
     *
     * @return 属性-冷却时间
     */
    protected abstract int initCooldownTime();

    /**
     * 初始化蓄力时间
     *
     * @return 属性-蓄力时间
     */
    protected abstract int initChargeTime();
}
