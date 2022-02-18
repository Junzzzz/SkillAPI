package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.CallbackPacket;
import skillapi.server.SkillServer;
import skillapi.skill.SkillProfile.SkillProfileInfo;
import skillapi.skill.SkillProfileManager;
import skillapi.skill.Skills;

import java.util.List;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket
public class RemoveProfilePacket extends CallbackPacket<List<SkillProfileInfo>> {
    private String name;

    @Override
    protected List<SkillProfileInfo> returns(EntityPlayer player, Side from) {
        if (from.isClient()) {
            if (SkillServer.hasHighestAuthority(player)) {
                SkillProfileManager profileManager = Skills.getProfileManager();
                profileManager.remove(name);
                return profileManager.getInfos();
            }
        }
        return null;
    }
}
