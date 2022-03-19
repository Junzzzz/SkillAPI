package skillapi.packet;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.AbstractPacket;
import skillapi.packet.serializer.PacketSerializer;
import skillapi.skill.AbstractSkill;
import skillapi.skill.Skills;

/**
 * @author Jun
 */
@AllArgsConstructor
@SkillPacket(serializer = ClientSkillUnleashPacket.Serializer.class)
public class ClientSkillUnleashPacket extends AbstractPacket {
    private final AbstractSkill skill;

    @Override
    protected void run(EntityPlayer player, Side from) {
        if (from.isServer()) {
            skill.clientUnleash(player);
        }
    }

    static class Serializer implements PacketSerializer<ClientSkillUnleashPacket> {
        @Override
        public void serialize(ClientSkillUnleashPacket packet, ByteBuf buffer) throws Exception {
            PacketSerializer.writeString(buffer, packet.skill.getUnlocalizedName());
        }

        @Override
        public ClientSkillUnleashPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
            AbstractSkill skill = Skills.get(PacketSerializer.readString(buffer));
            return new ClientSkillUnleashPacket(skill);
        }
    }
}
