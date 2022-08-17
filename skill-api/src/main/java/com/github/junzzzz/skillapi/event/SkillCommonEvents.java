package com.github.junzzzz.skillapi.event;

import com.github.junzzzz.skillapi.Application;
import com.github.junzzzz.skillapi.api.annotation.SkillEvent;
import com.github.junzzzz.skillapi.packet.ClientSkillInitPacket;
import com.github.junzzzz.skillapi.packet.base.Packet;
import com.github.junzzzz.skillapi.skill.PlayerSkills;
import com.github.junzzzz.skillapi.skill.Skills;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;

/**
 * @author Jun
 */
@SkillEvent
public class SkillCommonEvents {
    @SubscribeEvent
    public void onConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entity;
            addSkillProperties(player, Application.MOD_ID, new PlayerSkills());
        }
    }

    @SubscribeEvent
    public void onPlayerClone(Clone event) {
        PlayerSkills oldSkills = PlayerSkills.get(event.original);
        if (oldSkills != null) {
            addSkillProperties(event.entityPlayer, Application.MOD_ID, oldSkills);
        }
    }

    private void addSkillProperties(EntityPlayer player, String id, PlayerSkills properties) {
        PlayerSkills playerSkills = PlayerSkills.get(player);
        if (playerSkills == null) {
            player.registerExtendedProperties(id, properties);
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            properties.saveNBTData(tag);
            playerSkills.loadNBTData(tag);
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
