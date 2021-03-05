package skillapi.packets;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.PlayerSkills;
import skillapi.SkillRegistry;

public class LearnSkillPacket extends SkillPacket {
    protected int id;
    protected String skill;

    public LearnSkillPacket() {
    }

    public LearnSkillPacket(int id, String skill) {
        this.id = id;
        this.skill = skill;
    }

    @Override
    public String getChannel() {
        return SkillPacketHandler.CHANNELS[1];
    }

    @Override
    public void toBytes(ByteBuf out) {
        out.writeInt(id);
        byte[] temp;
        if (skill != null) {
            temp = skill.getBytes(Charsets.UTF_8);
            out.writeByte(temp.length);
            out.writeBytes(temp);
        } else {
            out.writeByte(-1);
        }
    }

    @Override
    public void fromBytes(ByteBuf in) {
        id = in.readInt();
        byte i = in.readByte();
        if (i > 0) {
            byte[] temp = new byte[i];
            in.readBytes(temp);
            skill = new String(temp, Charsets.UTF_8);
        }
    }

    @Override
    boolean run(EntityPlayer player) {
        if (player.getEntityId() == id && (SkillRegistry.isSkillRegistered(skill) || skill == null)) {
            PlayerSkills.get(player).skillJustLearnt = SkillRegistry.get(skill);
            if (skill != null && !SkillRegistry.isSkillKnown(player, skill)) {
                PlayerSkills.get(player).knownSkills.add(skill);
            }
        }
        return false;
    }
}
