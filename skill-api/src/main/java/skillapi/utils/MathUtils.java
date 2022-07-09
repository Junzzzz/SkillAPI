package skillapi.utils;

import net.minecraft.util.Vec3;

import javax.vecmath.Vector3d;

/**
 * @author Jun
 */
public class MathUtils {
    public static Vec3 tranform(Vector3d vector) {
        return Vec3.createVectorHelper(vector.x, vector.y, vector.z);
    }

    public static Vector3d tranform(Vec3 vector) {
        return new Vector3d(vector.xCoord, vector.yCoord, vector.zCoord);
    }
}
