package skillapi.skill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;
import skillapi.common.Message;

/**
 * @author Jun
 */
@SkillEffect
public class TestSkillEffect extends AbstractSkillEffect {
    @SkillParam
    private int testParam;

    @Override
    public boolean unleash(EntityPlayer player, EntityLivingBase entity) {
        if (entity != null) {
            Message.send(player,"Face to: " + entity.getCommandSenderName());
        } else {
            player.addChatComponentMessage(new ChatComponentText("param: " + testParam));
        }

        return true;
    }

    @Override
    public boolean canUnleash(EntityPlayer player, EntityLivingBase entity) {
        return true;
    }
}
