package genericskills.utils;

/**
 * @author Jun
 */
public class SkillUtils {
    /**
     * min max k
     * min <= motion < max
     * Distance = k * motion
     */
    private static final double[][] motionXZ = new double[][]{
            // N = 0
            {0, 0},
            // N = 1
            {0.005, 1},
            // N = 2
            {0.00915751, 1.546},
            // N = 3
            {0.016772, 1.84412},
            // N = 4
            {0.0307179, 2.00689},
            // N = 5
            {0.05626, 2.09576},
            // N = 6
            {0.10304, 2.14429},
            // N = 7
            {0.188718, 2.17078},
            // N = 8
            {0.345638, 2.18525},
            // N = 9
            {0.633037, 2.19314},
            // N = 10
            {1.15941, 2.19746},
            // N = 11
            {2.12346, 2.19981},
            // N = 12
            {3.88912, 2.2011},
            // N = 13
            {7.12292, 2.2018},
            // N = 14
            {13.0456, 2.2018},
            // N = 15
            {23.8931, 2.20239},
            // N = 16
            {43.7603, 2.20251},
            // N = 17
            {80.1471, 2.20257},
            // N = 18
            {146.79, 2.2026},
            // N = 19
            {268.845, 2.20262},
            // N = 20
            {492.391, 2.20263}
    };

    public static double getMotionXZ(double distance) {
        double absDistance = Math.abs(distance);
        if (absDistance <= 0.005) {
            return distance;
        }
        for (double[] range : motionXZ) {
            if (absDistance < range[0]) {
                return distance / range[1];
            }
        }
        return distance / 2.20264;
    }

    public static float getDamage(double damage) {
        damage = Math.round(damage);

        if (damage < 1.0D) {
            damage = 1.0D;
        }
        return (float) damage;
    }
}
