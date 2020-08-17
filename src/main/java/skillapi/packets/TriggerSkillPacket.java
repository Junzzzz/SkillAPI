package skillapi.packets;

import net.minecraft.entity.player.EntityPlayer;
import skillapi.PlayerSkills;
import skillapi.SkillRegistry;

public class TriggerSkillPacket extends UpdateSkillPacket {
    public TriggerSkillPacket() {
    }

    public TriggerSkillPacket(int id, int i, String name) {
        super(id, i, name);
    }

    @Override
    public String getChannel() {
        return SkillPacketHandler.CHANNELS[3];
    }

    @Override
    boolean run(EntityPlayer player) {
        if (player.getEntityId() == id && SkillRegistry.isSkillKnown(player, skill)) {
            if (PlayerSkills.get(player).skillBar[num] == SkillRegistry.get(skill)) {//Valid trigger packet
                PlayerSkills.get(player).skillBar[num].triggerSkill(player);
                //Send back checked info to client
                return !player.worldObj.isRemote;
            }
        }
        return false;
    }
}
