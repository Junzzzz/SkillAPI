package skillapi.packet.serializer;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.packet.ClientSkillInitPacket;
import skillapi.skill.SkillProfile;
import skillapi.utils.JsonUtils;

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
