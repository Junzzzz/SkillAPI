package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public abstract class BaseSkill {
    private int id;

    private int mana;
    private int cooldown;
    private int charge;

    private String name;

    public final int getId() {
        return id;
    }

    protected final void setId(int id) {
        this.id = id;
    }

    public final int getMana() {
        return mana;
    }

    protected final void setMana(int mana) {
        this.mana = mana;
    }

    public final int getCooldown() {
        return cooldown;
    }

    protected final void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public final int getCharge() {
        return charge;
    }

    protected final void setCharge(int charge) {
        this.charge = charge;
    }

    public final String getName() {
        return name;
    }

    protected final void setName(String name) {
        this.name = name;
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
