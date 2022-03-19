package skillapi.skill;

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
    public void effect(EntityPlayer player, SkillExtraInfo info) {
        player.addChatComponentMessage(new ChatComponentText("Static Skill!"));
    }
}
