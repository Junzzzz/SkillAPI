package genericskills.effects.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect
public abstract class AbstractBilateralParamLevelPotionEffect extends AbstractParamLevelPotionEffect {
    @SkillParam
    protected boolean self;

    public AbstractBilateralParamLevelPotionEffect(Potion potion) {
        super(potion);
    }

    @Override
    public boolean clientBeforeUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        if (this.self) {
            return true;
        }
        return super.clientBeforeUnleash(player, extraInfo);
    }

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (self) {
            player.addPotionEffect(new PotionEffect(this.potion.id, this.duration * 20, getLevel()));
        } else {
            target.addPotionEffect(new PotionEffect(this.potion.id, this.duration * 20, getLevel()));
        }
    }
}
