package genericskill.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import skillapi.potion.SkillPotion;

/**
 * @author Jun
 */
public class DamageBoostPotion extends SkillPotion {
    protected DamageBoostPotion(int id) {
        super(id, "damageBoost", false, 0x7CAFC6);
        setIconIndex(0, 4);
        // Type 2
        this.func_111184_a(SharedMonsterAttributes.attackDamage, "d91de893-9953-42c7-b5d2-138835ec6608", 0, 2);
    }

    @Override
    public double func_111183_a(int amplifier, AttributeModifier attributeModifier) {
        return amplifier / 100.0D;
    }
}
