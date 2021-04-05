package skillapi.skill;

import skillapi.common.SkillRuntimeException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @date 2021/4/5.
 */
public final class StaticSkillManager {
    private final static Map<String, BaseStaticSkill> STATIC_SKILL_MAP = new HashMap<>(16);

    public static void register(BaseStaticSkill skill) {
        if (STATIC_SKILL_MAP.put(skill.name, skill) != null) {
            throw new SkillRuntimeException("Duplicate skill name: %s", skill.name);
        }
    }

    public static List<BaseStaticSkill> getSkillList() {
        return new LinkedList<>(STATIC_SKILL_MAP.values());
    }
}
