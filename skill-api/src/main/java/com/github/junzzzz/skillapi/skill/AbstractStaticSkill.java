package com.github.junzzzz.skillapi.skill;

import com.github.junzzzz.skillapi.common.SkillRuntimeException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

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

    @Override
    public final void afterUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        // NOP
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

        @Deprecated
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
