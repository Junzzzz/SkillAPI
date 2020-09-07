package skillapi.skill;

import skillapi.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public final class SkillHandler {
    private static List<BaseSkill> skills = new ArrayList<BaseSkill>(16);
    private static Map<String, Integer> skillMap = new HashMap<String, Integer>(16);
    private static int size = 0;

    private static int lastRemovedId = -1;

    private static final Object MUTEX = new Object();

    public static void register(SkillConfig config) {
        for (SkillConfig.DynamicSkillConfig dynamicSkillConfig : config.getCustoms()) {
            final List<BaseSkillEffect> skillEffects = ListUtils.mapTo(dynamicSkillConfig.getEffects(), new ListUtils.Function<SkillConfig.SkillEffectConfig, BaseSkillEffect>() {
                @Override
                public BaseSkillEffect apply(SkillConfig.SkillEffectConfig skillEffectConfig) {
                    return SkillEffectHandler.getEffect(skillEffectConfig.getName(), skillEffectConfig.getPrams());
                }
            });

            register(new DynamicSkill(dynamicSkillConfig.getName(), skillEffects));
        }
    }

    public static boolean register(BaseSkill skill) {
        if (!skillMap.containsKey(skill.getName())) {
            return false;
        }
        synchronized (MUTEX) {
            // 填充上一个删除的技能
            final int id = lastRemovedId == -1 ? size++ : lastRemovedId;

            skill.setId(id);
            skills.add(id, skill);
            skillMap.put(skill.getName(), id);

            lastRemovedId = -1;
        }
        return true;
    }

    public static boolean unregister(int id) {
        if (!rangeCheck(id)) {
            return false;
        }
        synchronized (MUTEX) {
            final BaseSkill oldSkill = skills.get(id);
            skillMap.remove(oldSkill.getName());
            skills.set(id, null);
            lastRemovedId = id;
        }
        return true;
    }

    public static BaseSkill getSkill(int id) {
        if (!rangeCheck(id)) {
            return null;
        }
        return skills.get(id);
    }

    private static boolean rangeCheck(int id) {
        return id < size;
    }

    public static BaseSkill getSkill(String name) {
        synchronized (MUTEX) {
            Integer id;
            if ((id = skillMap.get(name)) == null) {
                return null;
            }
            return skills.get(id);
        }
    }

    public static int registeredSize() {
        return size;
    }

    public static boolean containsSkill(String name) {
        return skillMap.containsKey(name);
    }
}
