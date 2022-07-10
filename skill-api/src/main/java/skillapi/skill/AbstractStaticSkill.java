package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import skillapi.common.SkillRuntimeException;

/**
 * @author Jun
 */
public abstract class AbstractStaticSkill extends AbstractSkill {
    public AbstractStaticSkill() {
        init(new StaticSkillBuilder(this));

        // Default name
        this.unlocalizedName = Skills.PREFIX_STATIC + Skills.getModId(this.getClass()) + "." + this.getClass().getSimpleName();
    }

    protected abstract void init(StaticSkillBuilder builder);

    protected abstract void effect(EntityPlayer player, SkillExtraInfo info);

    @Override
    public final boolean unleash(EntityPlayer player, SkillExtraInfo info) {
        this.effect(player, info);
        return true;
    }

    static final class StaticSkillBuilder {
        AbstractStaticSkill skill;

        public StaticSkillBuilder(AbstractStaticSkill skill) {
            this.skill = skill;
        }

        StaticSkillBuilder mana(int mana) {
            if (mana < 0) {
                throw new SkillRuntimeException("Invalid skill parameter [mana]: %d", mana);
            }
            skill.mana = mana;
            return this;
        }

        StaticSkillBuilder cooldown(long cooldown) {
            if (cooldown < 0) {
                throw new SkillRuntimeException("Invalid skill parameter [cooldown]: %d", cooldown);
            }
            skill.cooldown = cooldown;
            return this;
        }

        StaticSkillBuilder charge(int charge) {
            if (charge < 0) {
                throw new SkillRuntimeException("Invalid skill parameter [charge]: %d", charge);
            }
            skill.charge = charge;
            return this;
        }

        StaticSkillBuilder icon(ResourceLocation iconResourceLocation) {
            skill.iconResource = iconResourceLocation;
            return this;
        }
    }
}
