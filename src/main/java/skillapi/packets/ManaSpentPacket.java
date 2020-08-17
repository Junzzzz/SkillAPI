package skillapi.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.PlayerSkills;

public class ManaSpentPacket extends SkillPacket {
    protected int id;
    protected int cost;

    public ManaSpentPacket(int entityId, int mana) {
        this.id = entityId;
        this.cost = mana;
    }

    public ManaSpentPacket() {
    }

    @Override
    public String getChannel() {
        return SkillPacketHandler.CHANNELS[6];
    }

    @Override
    public void toBytes(ByteBuf out) {
        out.writeInt(id);
        out.writeInt(cost);
    }

    @Override
    public void fromBytes(ByteBuf in) {
        id = in.readInt();
        cost = in.readInt();
    }

    @Override
    boolean run(EntityPlayer player) {
        if (player.getEntityId() == id) {
            PlayerSkills.get(player).spendMana(cost);
        }
        return false;
    }
}
