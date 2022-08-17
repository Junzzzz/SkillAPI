package com.github.junzzzz.skillapi.skill;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.junzzzz.skillapi.api.annotation.SkillPacket;
import com.github.junzzzz.skillapi.common.Message;
import com.github.junzzzz.skillapi.packet.base.AbstractPacket;
import com.github.junzzzz.skillapi.packet.serializer.PacketSerializer;
import com.github.junzzzz.skillapi.utils.JsonUtils;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map;

/**
 * @author Jun
 */
@SkillPacket(serializer = PlayerUnleashSkillPacket.Serializer.class)
public class PlayerUnleashSkillPacket extends AbstractPacket {
    private final int skillIndex;
    private final Map<String, SkillExtraInfo.ExtraObject> extraInfo;

    public PlayerUnleashSkillPacket(int skill, SkillExtraInfo extraInfo) {
        this.skillIndex = skill;
        this.extraInfo = extraInfo.getMap();
    }

    public PlayerUnleashSkillPacket(int skill, Map<String, SkillExtraInfo.ExtraObject> extraInfo) {
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
        if (skills.getMana() < skill.getMana()) {
            Message.send(player, "[服务端] 魔力不足: " + skills.getMana() + "/" + skill.getMana());
            return;
        }
        if (!cooldown.isCooledDown()) {
            Message.send(player, "[服务端] 技能未冷却");
        }
        skills.consumeMana(skill.getMana());
        cooldown.setCooling();
        SkillExecutor.execute(skill, player, SkillExtraInfo.get(extraInfo));
    }

    static class Serializer implements PacketSerializer<PlayerUnleashSkillPacket> {
        @Override
        public void serialize(PlayerUnleashSkillPacket packet, ByteBuf buffer) throws Exception {
            buffer.writeInt(packet.skillIndex);
            if (packet.extraInfo == null || packet.extraInfo.isEmpty()) {
                buffer.writeBoolean(false);
            } else {
                buffer.writeBoolean(true);
                PacketSerializer.writeString(buffer, JsonUtils.getMapper().writeValueAsString(packet.extraInfo));
            }
        }

        @Override
        public PlayerUnleashSkillPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
            int index = buffer.readInt();
            if (buffer.readBoolean()) {
                // Has extra info
                String json = PacketSerializer.readString(buffer);
                ObjectMapper mapper = JsonUtils.getMapper();
                JavaType mapType = mapper.getTypeFactory().constructParametrizedType(Map.class, Map.class, String.class, SkillExtraInfo.ExtraObject.class);
                Map<String, SkillExtraInfo.ExtraObject> map = mapper.readValue(json, mapType);
                return new PlayerUnleashSkillPacket(index, map);
            }
            return new PlayerUnleashSkillPacket(index, SkillExtraInfo.empty());
        }
    }
}
