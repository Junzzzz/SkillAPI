package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import skillapi.api.annotation.StaticSkill;
import skillapi.utils.ClientUtils;

import java.util.List;

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

    @Override
    public void clientUnleash(EntityPlayer player, SkillExtraInfo extraInfo) {
        super.clientUnleash(player, extraInfo);
        List<EntityLivingBase> entities = ClientUtils.getPointedDirectionEntitiesByBox(2, 1.8, 2);
        for (EntityLivingBase entity : entities) {
            System.out.println(entity.getCommandSenderName());
        }
    }
}
