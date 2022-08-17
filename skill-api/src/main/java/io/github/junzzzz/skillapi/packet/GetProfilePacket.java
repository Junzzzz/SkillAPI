package io.github.junzzzz.skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import io.github.junzzzz.skillapi.api.annotation.SkillPacket;
import io.github.junzzzz.skillapi.packet.base.CallbackPacket;
import io.github.junzzzz.skillapi.server.SkillServer;
import io.github.junzzzz.skillapi.skill.SkillProfile;
import io.github.junzzzz.skillapi.skill.Skills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;


/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
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
