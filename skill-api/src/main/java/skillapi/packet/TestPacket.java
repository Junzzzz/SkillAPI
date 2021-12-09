package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import skillapi.api.annotation.SkillPacket;

/**
 * @author Jun
 * @date 2020/8/26.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket
public class TestPacket extends AbstractPacket {
    private String testString;

    @Override
    void run(EntityPlayer player, Side from) {
        player.addChatComponentMessage(new ChatComponentText(testString));
    }
}
