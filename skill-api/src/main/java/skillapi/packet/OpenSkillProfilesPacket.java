package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.CallbackPacket;
import skillapi.server.SkillServer;
import skillapi.skill.SkillProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jun
 */
@NoArgsConstructor
@SkillPacket
public class OpenSkillProfilesPacket extends CallbackPacket<List<SkillProfile>> {
    @Override
    protected List<SkillProfile> returns(EntityPlayer player, Side from) {
        if (from.isClient()) {
            if (SkillServer.hasHighestAuthority(player)) {
                // TODO Add profile
                return new ArrayList<>();
            }
        }
        return null;
    }
}
