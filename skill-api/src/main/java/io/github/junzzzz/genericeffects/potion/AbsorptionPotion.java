package io.github.junzzzz.genericeffects.potion;

import io.github.junzzzz.skillapi.potion.SkillPotion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;

/**
 * @author Jun
 */
public class AbsorptionPotion extends SkillPotion {
    protected AbsorptionPotion(int id) {
        super(id, "absorption", false, 0x2552A5);
        setIconIndex(2, 2);
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap attributeMap, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float) amplifier);
        super.removeAttributesModifiersFromEntity(entity, attributeMap, amplifier);
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap attributeMap, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float) amplifier);
        super.applyAttributesModifiersToEntity(entity, attributeMap, amplifier);
    }
}
