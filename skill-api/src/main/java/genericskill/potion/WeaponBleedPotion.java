package genericskill.potion;

import skillapi.potion.SkillPotion;

/**
 * @author Jun
 */
public class WeaponBleedPotion extends SkillPotion {
    protected WeaponBleedPotion(int id) {
        super(id, "weaponBleed", true, 0x932423);
        setIconIndex(0, 4);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        // Do not use original function
        return false;
    }
}
