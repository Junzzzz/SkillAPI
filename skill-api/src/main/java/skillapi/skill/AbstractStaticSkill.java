package skillapi.skill;

import skillapi.common.SkillRuntimeException;

/**
 * @author Jun
 */
public abstract class AbstractStaticSkill extends AbstractSkill {
    public AbstractStaticSkill() {
        init(new StaticSkillBuilder());

        // Default name
        this.name = Skills.PREFIX_STATIC + Skills.getModId(this.getClass()) + "." + this.getClass().getSimpleName();
    }

    protected abstract void init(StaticSkillBuilder builder);

    final class StaticSkillBuilder {
        StaticSkillBuilder mana(int mana) {
            if (mana < 0) {
                throw new SkillRuntimeException("Invalid skill parameter [mana]: %d", mana);
            }
            AbstractStaticSkill.this.mana = mana;
            return this;
        }

        StaticSkillBuilder cooldown(long cooldown) {
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
