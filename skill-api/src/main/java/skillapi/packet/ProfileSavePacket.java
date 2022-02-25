package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.AbstractPacket;
import skillapi.server.SkillServer;
import skillapi.skill.SkillProfile;
import skillapi.skill.Skills;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket
public class ProfileSavePacket extends AbstractPacket {
    private SkillProfile profile;

    @Override
    protected void run(EntityPlayer player, Side from) {
        if (from.isClient()) {
            if (SkillServer.hasHighestAuthority(player)) {
                Skills.getProfileManager().saveProfile(profile);
            }
        }
    }
}
