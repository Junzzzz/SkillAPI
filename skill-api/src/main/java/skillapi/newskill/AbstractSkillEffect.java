package skillapi.newskill;

/**
 * @author Jun
 */
public abstract class AbstractSkillEffect implements SkillEffect {
    private String name;

    public String getName() {
        return this.name;
    }
}
