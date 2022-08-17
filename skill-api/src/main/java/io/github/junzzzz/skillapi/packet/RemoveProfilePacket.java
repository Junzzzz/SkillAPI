package io.github.junzzzz.skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import io.github.junzzzz.skillapi.api.annotation.SkillPacket;
import io.github.junzzzz.skillapi.packet.base.CallbackPacket;
import io.github.junzzzz.skillapi.server.SkillServer;
import io.github.junzzzz.skillapi.skill.SkillProfile.SkillProfileInfo;
import io.github.junzzzz.skillapi.skill.SkillProfileManager;
import io.github.junzzzz.skillapi.skill.Skills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

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
