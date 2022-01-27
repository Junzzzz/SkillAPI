package skillapi.packet;


import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.AbstractPacket;
import skillapi.packet.serializer.PacketSerializer;
import skillapi.skill.AbstractSkill;
import skillapi.skill.Cooldown;
import skillapi.skill.PlayerSkills;
import skillapi.skill.SkillExecutor;

/**
 * @author Jun
 */
@SkillPacket(serializer = PlayerUnleashSkillPacket.Serializer.class)
public class PlayerUnleashSkillPacket extends AbstractPacket {
    private final int skillIndex;
    private final int targetEntityId;

    public PlayerUnleashSkillPacket(int skill) {
        this(skill, null);
    }

    public PlayerUnleashSkillPacket(int skill, EntityLivingBase target) {
        this.skillIndex = skill;
        this.targetEntityId = target == null ? -1 : target.getEntityId();
    }

    public PlayerUnleashSkillPacket(int skill, int targetEntityId) {
        this.skillIndex = skill;
        this.targetEntityId = targetEntityId;
    }

    @Override
    protected void run(EntityPlayer player, Side from) {
        PlayerSkills skills = PlayerSkills.get(player);
        AbstractSkill skill = skills.getSkill(skillIndex);
        Cooldown cooldown = skills.getSkillCooldown(skillIndex);
        if (skill == null || cooldown == null) {
            return;
        }
        if (skills.getMana() > skill.getMana() && cooldown.isCooledDown()) {
            skills.consumeMana(skill.getMana());
            player.addChatComponentMessage(new ChatComponentText("consume: " + skill.getMana()));
            player.addChatComponentMessage(new ChatComponentText("current: " + skills.getMana()));
            if (targetEntityId < 0) {
                SkillExecutor.execute(skill, player, null);
            } else {
                Entity entity = player.getEntityWorld().getEntityByID(targetEntityId);
                SkillExecutor.execute(skill, player, (EntityLivingBase) entity);
            }
        }
    }

    static class Serializer implements PacketSerializer<PlayerUnleashSkillPacket> {
        @Override
        public void serialize(PlayerUnleashSkillPacket packet, ByteBuf buffer) {
            buffer.writeInt(packet.skillIndex);
            buffer.writeInt(packet.targetEntityId);
        }

        @Override
        public PlayerUnleashSkillPacket deserialize(Class<PlayerUnleashSkillPacket> packetClass, ByteBuf buffer) {
            return new PlayerUnleashSkillPacket(buffer.readInt(), buffer.readInt());
        }
    }
}
