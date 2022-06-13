package genericskill.potion;

import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
public class GenericSkillPotions {
    public static final Potion SKILL_HEAL = new SkillHealPotion(32);
    public static final Potion WEAPON_BLEED = new WeaponBleedPotion(33);
    public static final Potion MOVE_SPEED = new MoveSpeedPotion(34);
    public static final Potion DAMAGE_BOOST = new DamageBoostPotion(35);
    public static final Potion ABSORPTION = new AbsorptionPotion(36);
}
