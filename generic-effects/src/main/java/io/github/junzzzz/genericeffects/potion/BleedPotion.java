package io.github.junzzzz.genericeffects.potion;

import io.github.junzzzz.skillapi.potion.SkillPotion;

/**
 * @author Jun
 */
public class BleedPotion extends SkillPotion {
    protected BleedPotion(int id) {
        super(id, "bleed", true, 0x932423);
        setIconIndex(0, 4);
    }
}
