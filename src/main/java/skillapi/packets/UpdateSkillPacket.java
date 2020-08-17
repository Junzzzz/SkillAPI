package skillapi.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.PlayerSkills;
import skillapi.SkillAPI;
import skillapi.SkillRegistry;

public class UpdateSkillPacket extends LearnSkillPacket {
    protected int num;

    public UpdateSkillPacket() {
    }

    public UpdateSkillPacket(int id, int num, String skill) {
        super(id, skill);
        this.num = num;
    }

    @Override
    public String getChannel() {
        return SkillPacketHandler.CHANNELS[2];
    }

    @Override
    public void toBytes(ByteBuf out) {
        out.writeInt(num);
        super.toBytes(out);
    }

    @Override
    public void fromBytes(ByteBuf in) {
        num = in.readInt();
        super.fromBytes(in);
    }

    @Override
    boolean run(EntityPlayer player) {
        if (player.getEntityId() == id && (SkillRegistry.isSkillKnown(player, skill) || skill == null)) {//Valid update packet
            if (num >= 0) {
                PlayerSkills.get(player).skillBar[num] = SkillRegistry.get(skill);
            } else {
                PlayerSkills.get(player).chargingSkill = SkillRegistry.get(skill);
            }
            SkillAPI.proxy.updateKeyBindingTypes(player);
            //Send back checked info to client
            return !player.worldObj.isRemote;
        }
        return false;
    }
}
