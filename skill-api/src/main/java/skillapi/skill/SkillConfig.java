package skillapi.skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scheme of configuration skill execution
 *
 * @author Jun
 * @date 2021/4/5.
 */
public final class SkillConfig {
    private final BaseSkill[] skills;
    private final Map<String, BaseSkill> nameIndex;

    public SkillConfig(List<BaseSkill> skills) {
        this.skills = skills.toArray(new BaseSkill[0]);
        this.nameIndex = new HashMap<>(skills.size());
        for (BaseSkill skill : skills) {
            nameIndex.put(skill.getName(), skill);
        }
    }

    public BaseSkill getSkill(int index) {
        // In order to read quickly, no verification is done
        return this.skills[index];
    }

    public BaseSkill getSkill(String name) {
        return this.nameIndex.get(name);
    }

    public boolean contains(int index) {
        return this.skills.length > index && index > -1;
    }

    public boolean contains(String name) {
        return this.nameIndex.containsKey(name);
    }
}
