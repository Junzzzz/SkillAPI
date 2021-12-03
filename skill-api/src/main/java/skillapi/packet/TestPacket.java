package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import skillapi.api.annotation.SkillPacket;

/**
 * @author Jun
 * @date 2020/8/26.
 */
@SkillPacket
public class TestPacket extends AbstractPacket {
    private final String testString;

    public TestPacket(String testString) {
        this.testString = testString;
    }

    @Override
    void run(EntityPlayer player, Side from) {
        player.addChatComponentMessage(new ChatComponentText(testString));
    }
}
