package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.api.annotation.SkillPacket;
import skillapi.skill.DynamicSkillConfig;
import skillapi.skill.PlayerSkillProperties;
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
    private DynamicSkillConfig config;
    private NBTTagCompound playerSkillProperties;

    @Override
    void run(EntityPlayer player, Side from) {
        if (from.isServer()) {
            Skills.clientSwitchConfig(config);
            PlayerSkillProperties.get(player).loadNBTData(playerSkillProperties);
        }
    }
}
