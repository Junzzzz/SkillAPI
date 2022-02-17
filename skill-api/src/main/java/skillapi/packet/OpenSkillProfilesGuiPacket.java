package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.CallbackPacket;
import skillapi.server.SkillServer;
import skillapi.skill.SkillProfile.SkillProfileInfo;
import skillapi.skill.Skills;

import java.util.List;

/**
 * TODO 权限不足 不返回包 将会一直存放等待回调的函数
 *
 * @author Jun
 */
@NoArgsConstructor
@SkillPacket
public class OpenSkillProfilesGuiPacket extends CallbackPacket<List<SkillProfileInfo>> {
    @Override
    protected List<SkillProfileInfo> returns(EntityPlayer player, Side from) {
        if (from.isClient()) {
            if (SkillServer.hasHighestAuthority(player)) {
                return Skills.getProfileInfos();
            }
        }
        return null;
    }
}
