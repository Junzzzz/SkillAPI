package skillapi.newpacket;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.newpacket.base.BaseSkillPacket;
import skillapi.skill.SkillConfig;
import skillapi.skill.SkillHandler;

/**
 * @author Jun
 * @date 2020/9/10.
 */
@SkillPacket
public class SkillConfigPacket extends BaseSkillPacket {
    private SkillConfig config;

    public SkillConfigPacket(SkillConfig config) {
        this.config = config;
    }

    @Override
    protected void run(EntityPlayer player) {
        SkillHandler.register(config);
    }
}
