package skillapi.packet;


import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.skill.AbstractSkill;
import skillapi.skill.SkillExecutor;
import skillapi.skill.Skills;

import java.nio.charset.StandardCharsets;

/**
 * @author Jun
 */
@SkillPacket(serializer = PlayerUnleashSkillPacket.Serializer.class)
public class PlayerUnleashSkillPacket extends AbstractPacket {
    private final AbstractSkill skill;
    private final int targetEntityId;

    public PlayerUnleashSkillPacket(AbstractSkill skill, EntityLivingBase target) {
        this.skill = skill;
        this.targetEntityId = target.getEntityId();
    }

    public PlayerUnleashSkillPacket(AbstractSkill skill, int targetEntityId) {
        this.skill = skill;
        this.targetEntityId = targetEntityId;
    }

    @Override
    void run(EntityPlayer player, Side from) {
        if (targetEntityId < 0) {
            SkillExecutor.execute(skill, player, null);
        } else {
            Entity entity = player.getEntityWorld().getEntityByID(targetEntityId);
            SkillExecutor.execute(skill, player, (EntityLivingBase) entity);
        }
    }

    static class Serializer implements PacketSerializer<PlayerUnleashSkillPacket> {
        @Override
        public void serialize(PlayerUnleashSkillPacket packet, ByteBuf buffer) throws Exception {
            byte[] name = packet.skill.getUnlocalizedName().getBytes(StandardCharsets.UTF_8);
            buffer.writeInt(name.length);
            buffer.writeBytes(name);
            buffer.writeInt(packet.targetEntityId);
        }

        @Override
        public PlayerUnleashSkillPacket deserialize(Class<PlayerUnleashSkillPacket> packetClass, ByteBuf buffer) throws Exception {
            int i = buffer.readInt();
            byte[] bytes = new byte[i];
            buffer.readBytes(bytes);
            String skillName = new String(bytes, StandardCharsets.UTF_8);
            i = buffer.readInt();
            return new PlayerUnleashSkillPacket(Skills.get(skillName), i);
        }
    }
}
