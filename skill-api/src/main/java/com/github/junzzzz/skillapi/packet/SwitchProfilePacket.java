package com.github.junzzzz.skillapi.packet;

import com.github.junzzzz.skillapi.api.annotation.SkillPacket;
import com.github.junzzzz.skillapi.packet.base.AbstractPacket;
import com.github.junzzzz.skillapi.packet.base.Packet;
import com.github.junzzzz.skillapi.server.SkillServer;
import com.github.junzzzz.skillapi.skill.PlayerSkills;
import com.github.junzzzz.skillapi.skill.SkillExecutor;
import com.github.junzzzz.skillapi.skill.SkillProfile;
import com.github.junzzzz.skillapi.skill.Skills;
import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

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
            SkillProfile.SkillProfileInfo info = Skills.getCurrentProfileInfo();
            if (!info.getName().equals(profileName)) {
                SkillProfile profile = Skills.getProfile(profileName);
                SkillExecutor.stop();
                SkillServer.getManager().saveAllPlayerData();
                Skills.serverSwitchConfig(profile);
                for (EntityPlayerMP p : SkillServer.getPlayerList()) {
                    PlayerSkills.get(p).reload();
                    Packet.sendToClient(Skills.getInitPacket(p), p);
                }
                SkillExecutor.resume();
            }
        }
    }
}
