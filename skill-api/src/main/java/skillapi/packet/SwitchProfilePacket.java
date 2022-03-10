package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.AbstractPacket;
import skillapi.packet.base.Packet;
import skillapi.server.SkillServer;
import skillapi.skill.SkillExecutor;
import skillapi.skill.SkillProfile;
import skillapi.skill.SkillProfile.SkillProfileInfo;
import skillapi.skill.Skills;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket
public class SwitchProfilePacket extends AbstractPacket {
    private String profileName;

    @Override
    protected void run(EntityPlayer player, Side from) {
        if (from.isClient() && SkillServer.hasHighestAuthority(player)) {
            SkillProfileInfo info = Skills.getCurrentProfileInfo();
            if (!info.getName().equals(profileName)) {
                System.out.println("Change to: " + profileName);
                SkillProfile profile = Skills.getProfile(profileName);
                SkillExecutor.stop();
                SkillServer.getManager().saveAllPlayerData();
                Skills.serverSwitchConfig(profile);
                for (EntityPlayerMP p : SkillServer.getPlayerList()) {
                    Packet.sendToClient(Skills.getInitPacket(p), p);
                }
                SkillExecutor.resume();
            }
        }
    }
}
