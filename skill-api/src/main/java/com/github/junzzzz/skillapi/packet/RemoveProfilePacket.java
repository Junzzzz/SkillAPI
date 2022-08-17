package com.github.junzzzz.skillapi.packet;

import com.github.junzzzz.skillapi.api.annotation.SkillPacket;
import com.github.junzzzz.skillapi.packet.base.CallbackPacket;
import com.github.junzzzz.skillapi.server.SkillServer;
import com.github.junzzzz.skillapi.skill.SkillProfile.SkillProfileInfo;
import com.github.junzzzz.skillapi.skill.SkillProfileManager;
import com.github.junzzzz.skillapi.skill.Skills;
import cpw.mods.fml.relauncher.Side;
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
