package skillapi.skill;

import skillapi.utils.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public final class SkillHandler {
    private static final List<BaseSkill> SKILLS = new ArrayList<>(16);
    private static final Map<String, Integer> SKILL_MAP = new HashMap<>(16);
    private static int size = 0;

    private static int lastRemovedId = -1;

    private static final Object MUTEX = new Object();

    protected static void init() {
        synchronized (MUTEX) {
            SKILLS.clear();
            SKILL_MAP.clear();
            size = 0;
            lastRemovedId = -1;
        }
    }

    public static void register(SkillConfig config) {
        SkillHandler.init();

        for (SkillConfig.DynamicSkillConfig dynamicSkillConfig : config.getCustoms()) {
            final List<BaseSkillEffect> skillEffects = dynamicSkillConfig.getEffects().stream()
                    .map(skillEffectConfig -> SkillEffectHandler.getEffect(skillEffectConfig.getName(), skillEffectConfig.getPrams()))
                    .collect(Collectors.toList());

            register(new DynamicSkill(dynamicSkillConfig.getName(), skillEffects));
        }
    }

    public static boolean register(BaseSkill skill) {
        if (!SKILL_MAP.containsKey(skill.getName())) {
            return false;
        }
        synchronized (MUTEX) {
            // 填充上一个删除的技能
            final int id = lastRemovedId == -1 ? size++ : lastRemovedId;

            skill.setId(id);
            SKILLS.add(id, skill);
            SKILL_MAP.put(skill.getName(), id);

            lastRemovedId = -1;
        }
        return true;
    }

    public static boolean unregister(int id) {
        if (!rangeCheck(id)) {
            return false;
        }
        synchronized (MUTEX) {
            final BaseSkill oldSkill = SKILLS.get(id);
            SKILL_MAP.remove(oldSkill.getName());
            SKILLS.set(id, null);
            lastRemovedId = id;
        }
        return true;
    }

    public static BaseSkill getSkill(int id) {
        if (!rangeCheck(id)) {
            return null;
        }
        return SKILLS.get(id);
    }

    private static boolean rangeCheck(int id) {
        return id < size;
    }

    public static BaseSkill getSkill(String name) {
        synchronized (MUTEX) {
            Integer id;
            if ((id = SKILL_MAP.get(name)) == null) {
                return null;
            }
            return SKILLS.get(id);
        }
    }

    public static int registeredSize() {
        return size;
    }

    public static boolean containsSkill(String name) {
        return SKILL_MAP.containsKey(name);
    }

    public static List<BaseSkill> getSkills() {
        return SKILLS;
    }
}
