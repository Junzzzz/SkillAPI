package io.github.junzzzz.skillapi.packet.serializer;

import io.github.junzzzz.skillapi.packet.ClientSkillInitPacket;
import io.github.junzzzz.skillapi.skill.SkillProfile;
import io.github.junzzzz.skillapi.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Jun
 */
public class InitPacketSerializer implements PacketSerializer<ClientSkillInitPacket> {
    @Override
    public void serialize(ClientSkillInitPacket packet, ByteBuf buffer) throws Exception {
        byte[] config = JsonUtils.getMapper().writeValueAsBytes(packet.getConfig());
        buffer.writeInt(config.length);
        buffer.writeBytes(config);
        byte[] properties = CompressedStreamTools.compress(packet.getPlayerSkillProperties());
        buffer.writeInt(properties.length);
        buffer.writeBytes(properties);
    }

    @Override
    public ClientSkillInitPacket deserialize(Class<?> packetClass, ByteBuf buffer) throws Exception {
        ClientSkillInitPacket packet = new ClientSkillInitPacket();
        int length = buffer.readInt();
        byte[] config = new byte[length];
        buffer.readBytes(config);
        SkillProfile skillProfile = JsonUtils.getMapper().readValue(config, SkillProfile.class);

        length = buffer.readInt();
        byte[] properties = new byte[length];
        buffer.readBytes(properties);
        InputStream inputStream = new ByteArrayInputStream(properties);
        NBTTagCompound tag = CompressedStreamTools.readCompressed(inputStream);

        packet.setConfig(skillProfile);
        packet.setPlayerSkillProperties(tag);
        return packet;
    }
}
