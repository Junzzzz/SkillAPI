package skillapi.newpacket;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.newpacket.base.BaseSkillPacket;
import skillapi.skill.SkillHandler;
import skillapi.skill.SkillLocalConfig;

/**
 * @author Jun
 * @date 2020/9/10.
 */
@SkillPacket
public class SkillConfigPacket extends BaseSkillPacket {
    private final SkillLocalConfig config;

    public SkillConfigPacket(SkillLocalConfig config) {
        this.config = config;
    }

    @Override
    protected void run(EntityPlayer player) {
        SkillHandler.register(config);
    }
}
