package skillapi.skill;

import skillapi.common.SkillLog;
import skillapi.utils.ClassUtils;

import java.util.*;

/**
 * @author Jun
 * @date 2020/9/4.
 */
public final class SkillEffectHandler {
    private static Map<String, Class<? extends BaseSkillEffect>> effectMap = new HashMap<>(16);

    public static void register(String name, Class<? extends BaseSkillEffect> clz) {
        name = renameEffect(name);
        effectMap.put(name, clz);
    }

    private static String renameEffect(String name) {
        String newName = name;
        for (int i = 1; isRegistered(newName); i++) {
            newName = name + "-" + i;
        }
        if (!newName.equals(name)) {
            SkillLog.warn("The skill effect has the same name [%s] and has been automatically renamed to [%s]", name, newName);
        }
        return newName;
    }

    public static boolean isRegistered(String name) {
        return effectMap.get(name) != null;
    }

    public static BaseSkillEffect getEffect(String name, List<Integer> params) {
        final Class<? extends BaseSkillEffect> clz = effectMap.get(name);
        if (clz == null) {
            return null;
        }
        final BaseSkillEffect effect = ClassUtils.newEmptyInstance(clz, "Failed to instantiate skill effect");
        effect.init(params);
        return effect;
    }

    public static Collection<Class<? extends BaseSkillEffect>> getEffects() {
        return effectMap.values();
    }
}
