package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.api.annotation.SkillPacket;
import skillapi.client.SkillClient;
import skillapi.packet.base.AbstractPacket;
import skillapi.packet.serializer.InitPacketSerializer;
import skillapi.skill.PlayerSkills;
import skillapi.skill.SkillProfile;
import skillapi.skill.Skills;

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
            Skills.clientSwitchConfig(config);
            PlayerSkills skill = PlayerSkills.get(player);
            skill.loadNBTData(playerSkillProperties);
            SkillClient.initPlayerSkill(skill);
        }
    }
}
