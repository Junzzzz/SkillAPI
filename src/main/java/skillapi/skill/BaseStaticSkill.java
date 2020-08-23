package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Jun
 * @date 2020/8/23.
 */
public abstract class BaseStaticSkill extends BaseSkill {
    public void init() {
        super.setMana(initMana());
        super.setCooldown(initCooldownTime());
        super.setCharge(initChargeTime());
    }

    /**
     * 初始化需要消耗的魔法量
     *
     * @return 属性-魔法值
     */
    protected abstract int initMana();

    /**
     * 初始化冷却时间
     *
     * @return 属性-冷却时间
     */
    protected abstract int initCooldownTime();

    /**
     * 初始化蓄力时间
     *
     * @return 属性-蓄力时间
     */
    protected abstract int initChargeTime();
}
