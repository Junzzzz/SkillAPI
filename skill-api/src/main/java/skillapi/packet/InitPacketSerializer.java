package skillapi.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import skillapi.skill.DynamicSkillConfig;
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
    public ClientSkillInitPacket deserialize(Class<ClientSkillInitPacket> packetClass, ByteBuf buffer) throws Exception {
        ClientSkillInitPacket packet = new ClientSkillInitPacket();
        int length = buffer.readInt();
        byte[] config = new byte[length];
        buffer.readBytes(config);
        DynamicSkillConfig dynamicSkillConfig = JsonUtils.getMapper().readValue(config, DynamicSkillConfig.class);

        length = buffer.readInt();
        byte[] properties = new byte[length];
        buffer.readBytes(properties);
        InputStream inputStream = new ByteArrayInputStream(properties);
        NBTTagCompound tag = CompressedStreamTools.readCompressed(inputStream);

        packet.setConfig(dynamicSkillConfig);
        packet.setPlayerSkillProperties(tag);
        return packet;
    }
}
