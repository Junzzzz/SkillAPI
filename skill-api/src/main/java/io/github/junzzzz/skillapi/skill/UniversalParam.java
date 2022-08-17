package io.github.junzzzz.skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Sign for universal skill param
 *
 * @author Jun
 */
public final class UniversalParam implements SkillEffect {
    public static final UniversalParam INSTANCE = new UniversalParam();
    private static final String NAME = Skills.PREFIX_EFFECT + "universal";

    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        return false;
    }

    @Override
    public boolean canUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        return false;
    }

    @Override
    public void unleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        // NOP
    }

    @Override
    public void afterUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        // NOP
    }

    @Override
    public void clientUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        // NOP
    }

    @Override
    public String getUnlocalizedName() {
        return NAME;
    }

    public static String getNamePrefix() {
        return NAME;
    }
}
