package genericskill.potion;

import net.minecraft.potion.Potion;

/**
 * @author Jun
 */
public class GenericSkillPotions {
    public static final Potion SKILL_HEAL = new SkillHealPotion(32);
    public static final Potion BLEED = new BleedPotion(33);
    public static final Potion MOVE_SPEED = new MoveSpeedPotion(34);
    public static final Potion MOVE_SLOWDOWN = new MoveSlowdownPotion(35);
    public static final Potion DAMAGE_BOOST = new DamageBoostPotion(36);
    public static final Potion ABSORPTION = new AbsorptionPotion(37);
    public static final Potion WEAKNESS = new WeaknessPotion(38);
    public static final Potion IGNITE = new IgnitePotion(39);
}
