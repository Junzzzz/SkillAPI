package genericskills.potion;

import skillapi.potion.SkillPotion;

/**
 * @author Jun
 */
public class IgnitePotion extends SkillPotion {
    protected IgnitePotion(int id) {
        super(id, "ignite", true, 0x7CAFC6);
        setIconIndex(1, 7);
    }
}
