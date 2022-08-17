package com.github.junzzzz.genericeffects.potion;

import com.github.junzzzz.skillapi.potion.SkillPotion;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;

/**
 * @author Jun
 */
public class MoveSpeedPotion extends SkillPotion {
    protected MoveSpeedPotion(int id) {
        super(id, "moveSpeed", false, 0x7CAFC6);

        setIconIndex(0, 0);
        // Type 2
        this.func_111184_a(SharedMonsterAttributes.movementSpeed, "e294bc3f-3df4-4207-8dc2-9f11c5404ceb", 0, 2);
    }

    @Override
    public double func_111183_a(int amplifier, AttributeModifier attributeModifier) {
        return amplifier / 100.0D;
    }
}
