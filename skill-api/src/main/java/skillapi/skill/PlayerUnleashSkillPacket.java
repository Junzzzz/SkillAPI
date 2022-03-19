package skillapi.skill;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillPacket;
import skillapi.packet.base.AbstractPacket;
import skillapi.packet.serializer.PacketSerializer;
import skillapi.skill.SkillExtraInfo.ExtraObject;
import skillapi.utils.JsonUtils;

import java.util.Map;

/**
 * @author Jun
 */
@SkillPacket(serializer = PlayerUnleashSkillPacket.Serializer.class)
public class PlayerUnleashSkillPacket extends AbstractPacket {
    private final int skillIndex;
    private final Map<String, ExtraObject> extraInfo;

    public PlayerUnleashSkillPacket(int skill, SkillExtraInfo extraInfo) {
        this.skillIndex = skill;
        this.extraInfo = extraInfo.getMap();
    }

    public PlayerUnleashSkillPacket(int skill, Map<String, ExtraObject> extraInfo) {
        this.skillIndex = skill;
        this.extraInfo = extraInfo;
    }

    @Override
    protected void run(EntityPlayer player, Side from) {
        PlayerSkills skills = PlayerSkills.get(player);
        AbstractSkill skill = skills.getSkill(skillIndex);
        Cooldown cooldown = skills.getSkillCooldown(skillIndex);
        if (skill == null || cooldown == null) {
            return;
        }
        if (skills.getMana() >= skill.getMana() && cooldown.isCooledDown()) {
            skills.consumeMana(skill.getMana());

            SkillExecutor.execute(skill, player, SkillExtraInfo.get(extraInfo));

        }
    }

    static class Serializer implements PacketSerializer<PlayerUnleashSkillPacket> {
        @Override
        public void serialize(PlayerUnleashSkillPacket packet, ByteBuf buffer) throws Exception {
            buffer.writeInt(packet.skillIndex);
            if (packet.extraInfo == null || packet.extraInfo.isEmpty()) {
                buffer.writeBoolean(false);
            } else {
                buffer.writeBoolean(true);
                JsonUtils.getMapper().writeValueAsString(packet.extraInfo);
            }
        }

        @Override
        public PlayerUnleashSkillPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
            int index = buffer.readInt();
            if (buffer.readBoolean()) {
                // Has extra info
                String json = PacketSerializer.readString(buffer);
                ObjectMapper mapper = JsonUtils.getMapper();
                JavaType mapType = mapper.getTypeFactory().constructParametrizedType(Map.class, Map.class, String.class, ExtraObject.class);
                Map<String, ExtraObject> map = mapper.readValue(json, mapType);
                return new PlayerUnleashSkillPacket(index, map);
            }
            return new PlayerUnleashSkillPacket(index, SkillExtraInfo.empty());
        }
    }
}
