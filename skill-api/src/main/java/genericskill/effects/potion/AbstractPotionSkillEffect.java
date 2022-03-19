package genericskill.effects.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
public abstract class AbstractPotionSkillEffect extends AbstractTargetSkillEffect {
    @SkillParam
    protected int duration;
    private final Potion potion;
    private final int level;

    public AbstractPotionSkillEffect(Potion potion, int level) {
        this.potion = potion;
        this.level = level;
    }

    public AbstractPotionSkillEffect(Potion potion) {
        this.potion = potion;
        this.level = 2;
    }

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        target.addPotionEffect(new PotionEffect(this.potion.id, this.duration * 20, this.level));
        return true;
    }
}
