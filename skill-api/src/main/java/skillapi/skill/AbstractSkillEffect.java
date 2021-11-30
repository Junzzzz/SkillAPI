package skillapi.skill;

import skillapi.api.SkillApi;

import java.util.Objects;

/**
 * @author Jun
 */
public abstract class AbstractSkillEffect implements SkillEffect {
    private final String name = "skill.effect." + SkillApi.getModId(getClass()) + "." + getClass().getSimpleName();

    @Override
    public String getName() {
        return name;
    }

    public String getParamName(String param) {
        return name + "." + param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractSkillEffect that = (AbstractSkillEffect) o;

        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
