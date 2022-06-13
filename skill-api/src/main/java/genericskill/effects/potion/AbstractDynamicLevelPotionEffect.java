package genericskill.effects.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect
public abstract class AbstractDynamicLevelPotionEffect extends AbstractTargetSkillEffect {
    @SkillParam
    protected int duration;

    protected final Potion potion;

    public AbstractDynamicLevelPotionEffect(Potion potion) {
        this.potion = potion;
    }

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        target.addPotionEffect(new PotionEffect(this.potion.id, this.duration * 20, getLevel()));
        return true;
    }

    protected abstract int getLevel();
}
