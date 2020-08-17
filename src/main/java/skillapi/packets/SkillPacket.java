package skillapi.packets;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;

public abstract class SkillPacket implements IMessage {
    public final FMLProxyPacket getPacket(Side side) {
        ByteBuf buf = Unpooled.buffer();
        toBytes(buf);
        FMLProxyPacket p = new FMLProxyPacket(buf, getChannel());
        p.setTarget(side);
        return p;
    }

    public abstract String getChannel();

    abstract boolean run(EntityPlayer player);
}
