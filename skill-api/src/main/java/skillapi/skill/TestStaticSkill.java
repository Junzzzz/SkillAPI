package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import skillapi.api.annotation.StaticSkill;

/**
 * @author Jun
 */
@StaticSkill
public class TestStaticSkill extends AbstractStaticSkill {
    @Override
    protected void init(StaticSkillBuilder builder) {
        builder.mana(1)
                .cooldown(1)
                .charge(1);
    }

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase entity) {
        player.addChatComponentMessage(new ChatComponentText("Static Skill!"));
        return true;
    }

    @Override
    public boolean canUnleash(EntityPlayer player, EntityLivingBase entity) {
        return true;
    }
}
