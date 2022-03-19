package genericskill.utils;

/**
 * @author Jun
 */
public class SkillUtils {
    public static float getDamage(double damage) {
        damage = Math.round(damage);

        if (damage < 1.0D) {
            damage = 1.0D;
        }
        return (float) damage;
    }
}
