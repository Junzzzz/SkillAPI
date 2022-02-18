package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.AbstractPacket;
import skillapi.server.SkillServer;
import skillapi.skill.SkillProfile;
import skillapi.skill.Skills;

/**
 * @author Jun
 */
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
