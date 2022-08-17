package com.github.junzzzz.skillapi.packet;

import com.github.junzzzz.skillapi.api.annotation.SkillPacket;
import com.github.junzzzz.skillapi.packet.base.CallbackPacket;
import com.github.junzzzz.skillapi.server.SkillServer;
import com.github.junzzzz.skillapi.skill.SkillProfile.SkillProfileInfo;
import com.github.junzzzz.skillapi.skill.Skills;
import cpw.mods.fml.relauncher.Side;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * TODO 权限不足 不返回包 将会一直存放等待回调的函数
 *
 * @author Jun
 */
@NoArgsConstructor
@SkillPacket
public class GetProfileInfosPacket extends CallbackPacket<List<SkillProfileInfo>> {
    @Override
    protected List<SkillProfileInfo> returns(EntityPlayer player, Side from) {
        if (from.isClient()) {
            if (SkillServer.hasHighestAuthority(player)) {
                return Skills.getProfileManager().getInfos();
            }
        }
        return null;
    }
}
