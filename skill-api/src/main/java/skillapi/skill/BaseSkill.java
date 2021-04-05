package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public abstract class BaseSkill {
    protected String name;

    protected int mana;
    protected int cooldown;
    protected int charge;

    public final int getMana() {
        return mana;
    }

    public final int getCooldown() {
        return cooldown;
    }

    public final int getCharge() {
        return charge;
    }

    public final String getName() {
        return name;
    }

    /**
     * Preconditions for players to use skills
     *
     * @param player The players using skills
     * @return Returns {@code true} if the player can use the skill, otherwise returns {@code false}.
     */
    public abstract boolean canUse(EntityPlayer player);

    /**
     * Use skills
     *
     * @param player The players using skills
     */
    public abstract void doSkill(EntityPlayer player);
}
