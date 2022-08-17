package com.github.junzzzz.genericeffects.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.function.BiConsumer;

/**
 * @author Jun
 */
public class DynamicPotionEffect extends PotionEffect {
    private final BiConsumer<DynamicPotionEffect, EntityLivingBase> trigger;
    private final int perTick;
    private int duration;

    public DynamicPotionEffect(Potion potion, int tickDuration, int perTick, BiConsumer<DynamicPotionEffect, EntityLivingBase> trigger) {
        super(potion.id, tickDuration, 0);
        this.trigger = trigger;
        this.perTick = perTick > 0 ? perTick : 20;
        this.duration = tickDuration;
    }

    private void decreaseDuration() {
        this.duration--;
    }

    private boolean isReady() {
        return this.duration % this.perTick == 0;
    }

    public void clear() {
        this.duration = 0;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public boolean onUpdate(EntityLivingBase entity) {
        if (this.duration > 0) {
            if (isReady()) {
                this.performEffect(entity);
            }
            decreaseDuration();
        }

        return this.duration > 0;
    }

    @Override
    public void performEffect(EntityLivingBase entity) {
        if (this.duration > 0 && trigger != null) {
            trigger.accept(this, entity);
        }
    }
}
