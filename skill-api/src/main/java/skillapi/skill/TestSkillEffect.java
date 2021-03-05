package skillapi.skill;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEffect;

import java.util.Map;

/**
 * @author Jun
 * @date 2020/11/3.
 */
@SkillEffect
public class TestSkillEffect extends BaseSkillEffect{
    @Override
    protected String initName() {
        return "Test";
    }

    @Override
    protected int initParamNum() {
        return 1;
    }

    @Override
    protected void effect(EntityPlayer player, Map<String, Object> params) {

    }
}
