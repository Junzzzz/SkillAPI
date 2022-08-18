package io.github.junzzzz.genericeffects.potion;

import io.github.junzzzz.skillapi.potion.SkillPotion;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author Jun
 */
public class SkillHealPotion extends SkillPotion {
    protected SkillHealPotion(int id) {
        super(id, "skillHeal", true, 0x932423);
        setIconIndex(0, 7);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        // Per second
        return duration % 20 == 0;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        // Use amplifier to indicate the amount of life restored
        entity.heal(amplifier);
    }
}
