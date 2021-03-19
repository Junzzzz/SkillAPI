package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.annotation.SkillParam;
import skillapi.api.annotation.SkillEffect;

import java.util.Map;

/**
 * @author Jun
 * @date 2020/11/3.
 */
@SkillEffect
public class TestSkillEffect extends BaseSkillEffect {
    @SkillParam
    private int testParam;

    @Override
    protected void effect(EntityPlayer player) {
        System.out.println(player.getDisplayName() + ": " + testParam);
    }
}
