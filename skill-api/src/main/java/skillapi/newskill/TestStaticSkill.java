package skillapi.newskill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.StaticSkill;

/**
 * @author Jun
 */
@StaticSkill
public class TestStaticSkill extends AbstractStaticSkill {
    @Override
    protected void init(StaticSkillBuilder builder) {
        builder.name("test")
                .mana(1)
                .cooldown(1)
                .charge(1);
    }

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase entity) {

    }
}
