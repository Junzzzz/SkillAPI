package io.github.junzzzz.skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import io.github.junzzzz.skillapi.api.annotation.SkillPacket;
import io.github.junzzzz.skillapi.client.SkillClient;
import io.github.junzzzz.skillapi.packet.base.AbstractPacket;
import io.github.junzzzz.skillapi.packet.serializer.InitPacketSerializer;
import io.github.junzzzz.skillapi.skill.PlayerSkills;
import io.github.junzzzz.skillapi.skill.SkillProfile;
import io.github.junzzzz.skillapi.skill.Skills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Jun
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SkillPacket(serializer = InitPacketSerializer.class)
public class ClientSkillInitPacket extends AbstractPacket {
    private SkillProfile config;
    private NBTTagCompound playerSkillProperties;

    @Override
    protected void run(EntityPlayer player, Side from) {
        if (from.isServer()) {
            SkillClient.drawHud = false;
            Skills.clientSwitchConfig(config);
            PlayerSkills skill = PlayerSkills.get(player);
            skill.loadNBTData(playerSkillProperties);
            SkillClient.initPlayerSkill(skill);
            SkillClient.drawHud = true;
        }
    }
}
