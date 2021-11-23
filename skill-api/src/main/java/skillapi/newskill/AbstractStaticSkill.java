package skillapi.newskill;

import skillapi.api.SkillApi;
import skillapi.common.SkillRuntimeException;

/**
 * @author Jun
 */
public abstract class AbstractStaticSkill extends AbstractSkill {
    private static final String STATIC_SKILL_SUFFIX1 = "StaticSkill";
    private static final String STATIC_SKILL_SUFFIX2 = "Skill";

    public AbstractStaticSkill() {
        init(new StaticSkillBuilder());

        // Default name
        if (name == null || name.isEmpty()) {
            String defaultName = this.getClass().getSimpleName();
            int index;
            if ((index = defaultName.lastIndexOf(STATIC_SKILL_SUFFIX1)) == -1) {
                index = defaultName.lastIndexOf(STATIC_SKILL_SUFFIX2);
            }
            if (index != -1) {
                defaultName = defaultName.substring(0, index);
            }
            this.name = "skill.static." + SkillApi.getModId(this.getClass()) + "." + defaultName;
        }
    }

    protected abstract void init(StaticSkillBuilder builder);

    final class StaticSkillBuilder {
        StaticSkillBuilder name(String name) {
            if (name == null || name.isEmpty() || !name.matches(AbstractSkill.REGEX_NAME)) {
                throw new SkillRuntimeException("Invalid skill name: %s", name);
            }
            AbstractStaticSkill.this.name = "skill.static." + SkillApi.getModId(this.getClass()) + "." + name;
            return this;
        }

        StaticSkillBuilder mana(int mana) {
            if (mana < 0) {
                throw new SkillRuntimeException("Invalid skill parameter [mana]: %d", mana);
            }
            AbstractStaticSkill.this.mana = mana;
            return this;
        }

        StaticSkillBuilder cooldown(int cooldown) {
            if (cooldown < 0) {
                throw new SkillRuntimeException("Invalid skill parameter [cooldown]: %d", cooldown);
            }
            AbstractStaticSkill.this.cooldown = cooldown;
            return this;
        }

        StaticSkillBuilder charge(int charge) {
            if (charge < 0) {
                throw new SkillRuntimeException("Invalid skill parameter [charge]: %d", charge);
            }
            AbstractStaticSkill.this.charge = charge;
            return this;
        }
    }
}
