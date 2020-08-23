package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public abstract class BaseSkill {
    private int mana;
    private int cooldown;
    private int charge;

    public int getMana() {
        return mana;
    }

    protected void setMana(int mana) {
        this.mana = mana;
    }

    public int getCooldown() {
        return cooldown;
    }

    protected void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getCharge() {
        return charge;
    }

    protected void setCharge(int charge) {
        this.charge = charge;
    }


    /**
     * 是否能释放技能
     *
     * @param player 玩家
     * @return 判断结果
     */
    public abstract boolean canUse(EntityPlayer player);

    public abstract void doSkill(EntityPlayer player);
}
