package skillapi.skill;

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
     * 初始化技能名（仅允许英文）
     *
     * @return 属性-技能吗
     */
    protected abstract String initName();

    /**
     * 初始化需要消耗的魔法量
     *
     * @return 属性-魔法值
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
