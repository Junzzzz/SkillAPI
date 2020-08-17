package skillapi.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.PlayerSkills;
import skillapi.SkillRegistry;

public class ActiveSkillPacket extends LearnSkillPacket {
    private boolean active;

    public ActiveSkillPacket() {
    }

    public ActiveSkillPacket(int id, String skill, boolean active) {
        super(id, skill);
        this.active = active;
    }

    @Override
    public String getChannel() {
        return SkillPacketHandler.CHANNELS[4];
    }

    @Override
    public void toBytes(ByteBuf out) {
        super.toBytes(out);
        out.writeBoolean(active);
    }

    @Override
    public void fromBytes(ByteBuf in) {
        super.fromBytes(in);
        active = in.readBoolean();
    }

    @Override
    boolean run(EntityPlayer player) {
        if (player.getEntityId() == id && SkillRegistry.isSkillRegistered(skill)) {
            if (active) {
                PlayerSkills.get(player).activeSkills.add(skill);
            } else {
                PlayerSkills.get(player).activeSkills.remove(skill);
            }
        }
        return false;
    }
}
