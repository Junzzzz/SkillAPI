package skillapi.newskill;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import skillapi.api.annotation.SkillEffect;
import skillapi.api.annotation.SkillParam;

/**
 * @author Jun
 */
@SkillEffect
public class TestSkillEffect extends AbstractSkillEffect {
    @SkillParam
    private int testParam;

    @Override
    public void unleash(EntityPlayer player, EntityLivingBase entity) {
        player.addChatComponentMessage(new ChatComponentText("param: " + testParam));
    }
}
