package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.CallbackPacket;
import skillapi.server.SkillServer;
import skillapi.skill.SkillProfile;
import skillapi.skill.Skills;


/**
 * @author Jun
 */
@Getter
@Setter
@AllArgsConstructor
@SkillPacket
public class GetProfilePacket extends CallbackPacket<SkillProfile> {
    private String profileName;

    @Override
    protected SkillProfile returns(EntityPlayer player, Side from) {
        if (from.isClient()) {
            if (SkillServer.hasHighestAuthority(player)) {
                return Skills.getProfile(profileName);
            }
        }
        return null;
    }
}
