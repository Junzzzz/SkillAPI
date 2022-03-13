package skillapi.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import skillapi.Application;
import skillapi.api.annotation.SkillEvent;
import skillapi.packet.ClientSkillInitPacket;
import skillapi.packet.base.Packet;
import skillapi.skill.PlayerSkills;
import skillapi.skill.Skills;

/**
 * @author Jun
 */
@SkillEvent
public class SkillCommonEvents {
    @SubscribeEvent
    public void onConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            if (PlayerSkills.get(player) == null) {
                player.registerExtendedProperties(Application.MOD_ID, new PlayerSkills());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(Clone event) {
        PlayerSkills oldSkills = PlayerSkills.get(event.original);
        if (oldSkills != null) {
            event.entityPlayer.registerExtendedProperties(Application.MOD_ID, oldSkills);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.player.worldObj.isRemote) {
            // Server
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            // Restoring client data after death
            ClientSkillInitPacket initPacket = Skills.getInitPacket(player);
            Packet.send(initPacket, player);
        }
    }
}
