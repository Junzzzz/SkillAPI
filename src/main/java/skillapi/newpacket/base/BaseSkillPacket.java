package skillapi.newpacket.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import skillapi.common.SkillLog;
import skillapi.utils.JsonUtils;

/**
 * @author Jun
 * @date 2020/8/25.
 */
public abstract class BaseSkillPacket {
    public final void sendToClient(EntityPlayerMP player) {
        final FMLProxyPacket packet = processPacket(Side.CLIENT);
        if (packet == null) {
            return;
        }
        SkillPacketHandler.CHANNEL.sendTo(packet, player);
    }

    public final void sendToServer() {
        final FMLProxyPacket packet = processPacket(Side.SERVER);
        if (packet == null) {
            return;
        }
        SkillPacketHandler.CHANNEL.sendToServer(packet);
    }

    public final FMLProxyPacket processPacket(Side target) {
        ByteBuf buf = Unpooled.buffer();
        final String packetName = SkillPacketHandler.getPacketName(this.getClass());
        if (packetName == null) {
            SkillLog.error("This packet is not registered: %s", this.getClass().getName());
            return null;
        }
        final byte[] nameData = packetName.getBytes(Charsets.UTF_8);
        buf.writeByte(nameData.length);
        buf.writeBytes(nameData);

        try {
            JsonUtils.getMapper().writeValueAsString(this);
            buf.writeBytes(JsonUtils.getMapper().writeValueAsBytes(this));
        } catch (JsonProcessingException e) {
            buf.clear();
            SkillLog.error("Data processing failed!", e);
            return null;
        }

        final FMLProxyPacket fmlProxyPacket = new FMLProxyPacket(buf, SkillPacketHandler.CHANNEL_NAME);
        fmlProxyPacket.setTarget(target);
        return fmlProxyPacket;
    }

    /**
     * 收到数据包后执行的方法
     *
     * @param player 执行效果的玩家
     */
    protected abstract void run(EntityPlayer player);
}
