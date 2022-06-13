package skillapi.potion;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.potion.Potion;
import skillapi.common.SkillRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Jun
 */
public class SkillPotions {
    private static final int startIndex = 32;

    public static void insurePotionSize(int size) {
        if (Potion.potionTypes.length >= size) {
            return;
        }
        Potion[] oldPotions = Potion.potionTypes;
        Potion[] newPotions = new Potion[size];
        System.arraycopy(oldPotions, 0, newPotions, 0, oldPotions.length);

        Field potionTypesField = ReflectionHelper.findField(Potion.class, "potionTypes", "field_76425_a");
        potionTypesField.setAccessible(true);

        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(potionTypesField, potionTypesField.getModifiers() & ~Modifier.FINAL);

            potionTypesField.set(null, newPotions);

            modifiersField.setInt(potionTypesField, potionTypesField.getModifiers() & Modifier.FINAL);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new SkillRuntimeException("Failed to add potion capacity", e);
        }
    }
//
//    public static Potion getPotion(Class<? extends SkillPotion> clz) {
//
//    }

    public static Potion getPotion(int index) {
        return Potion.potionTypes[index];
    }
}
