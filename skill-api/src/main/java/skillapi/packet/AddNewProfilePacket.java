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
import skillapi.skill.SkillProfile;
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
public class AddNewProfilePacket extends CallbackPacket<List<SkillProfileInfo>> {
    private String name;

    @Override
    protected List<SkillProfileInfo> returns(EntityPlayer player, Side from) {
        if (from.isClient()) {
            if (SkillServer.hasHighestAuthority(player)) {
                SkillProfileManager profileManager = Skills.getProfileManager();
                name = getUnrepeatableName(profileManager, name);
                SkillProfile skillProfile = new SkillProfile(name, player.getCommandSenderName(), System.currentTimeMillis());
                profileManager.saveProfile(skillProfile);
                return profileManager.getInfos();
            }
        }
        return null;
    }

    private String getUnrepeatableName(SkillProfileManager manager, String origin) {
        int i = 1;
        String name = origin;
        while (manager.contains(name)) {
            name = origin + " (" + i + ")";
            i++;
        }
        return name;
    }
}
