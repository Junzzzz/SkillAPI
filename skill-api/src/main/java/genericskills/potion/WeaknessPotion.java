package genericskills.potion;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import skillapi.potion.SkillPotion;

/**
 * @author Jun
 */
public class WeaknessPotion extends SkillPotion {
    protected WeaknessPotion(int id) {
        super(id, "weakness", true, 0x587653);
        setIconIndex(0, 5);
        // Type 0
        this.func_111184_a(SharedMonsterAttributes.attackDamage, "4a8e9e5a-d2f5-4373-a56f-77aaffdf700d", 0, 0);
    }

    @Override
    public double func_111183_a(int amplifier, AttributeModifier attributeModifier) {
        return -amplifier;
    }
}
