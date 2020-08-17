package skillapi.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.PlayerSkills;
import skillapi.Skill;
import skillapi.SkillRegistry;

public class TickDataSkillPacket extends UpdateSkillPacket {
    private float time;
    private int charge;

    public TickDataSkillPacket() {
    }

    public TickDataSkillPacket(int id, Skill skill) {
        super(id, skill.cooldownFrame, skill.getName());
        this.charge = skill.charge;
        this.time = skill.timeLeft;
    }

    @Override
    public String getChannel() {
        return SkillPacketHandler.CHANNELS[5];
    }

    @Override
    public void toBytes(ByteBuf out) {
        super.toBytes(out);
        out.writeFloat(time);
        out.writeInt(charge);
    }

    @Override
    public void fromBytes(ByteBuf in) {
        super.fromBytes(in);
        time = in.readFloat();
        charge = in.readInt();
    }

    @Override
    boolean run(EntityPlayer player) {
        if (player.getEntityId() == id && SkillRegistry.isSkillKnown(player, skill)) {//Valid update packet
            Skill skil = SkillRegistry.get(skill);
            skil.charge = charge;
            if (charge == 0 && PlayerSkills.get(player).chargingSkill != null && PlayerSkills.get(player).chargingSkill.getName().equals(skill)) {
                PlayerSkills.get(player).chargingSkill = null;
            }
            skil.timeLeft = time;
            skil.cooldownFrame = num;
        }
        return false;
    }
}
