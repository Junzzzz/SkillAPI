package genericskill.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import skillapi.potion.SkillPotion;

/**
 * @author Jun
 */
public class MoveSlowdownPotion extends SkillPotion {
    protected MoveSlowdownPotion(int id) {
        super(id, "slowdown", false, 0x7CAFC6);

        setIconIndex(0, 1);
        // Type 2
        this.func_111184_a(SharedMonsterAttributes.movementSpeed, "97f18634-48da-42e6-a23f-268ba4481b7b", 0, 2);
    }

    @Override
    public double func_111183_a(int amplifier, AttributeModifier attributeModifier) {
        return -amplifier / 100.0D;
    }
}
