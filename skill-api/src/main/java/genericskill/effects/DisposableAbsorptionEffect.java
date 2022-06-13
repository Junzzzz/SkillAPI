package genericskill.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.skill.AbstractTargetSkillEffect;
import skillapi.skill.SkillExtraInfo;

/**
 * @author Jun
 */
@SkillEffect(callSuper = true)
public class DisposableAbsorptionEffect extends AbstractTargetSkillEffect {
    @SkillParam
    private int amount;

    @SkillParam
    private boolean self;

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase target, SkillExtraInfo extraInfo) {
        if (self) {
            player.setAbsorptionAmount(player.getAbsorptionAmount() + this.amount);
        } else {
            target.setAbsorptionAmount(player.getAbsorptionAmount() + this.amount);
        }
        return true;
    }
}
