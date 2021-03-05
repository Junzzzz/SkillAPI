package skillapi.newpacket;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.common.SkillLog;
import skillapi.newpacket.base.BaseSkillPacket;

/**
 * @author Jun
 * @date 2020/8/26.
 */
@SkillPacket
public class TestPacket extends BaseSkillPacket {
    private String testString;

    public TestPacket(String testString) {
        this.testString = testString;
    }

    @Override
    protected void run(EntityPlayer player) {
        System.out.println(player.getDisplayName() + ":" + testString);
    }
}
