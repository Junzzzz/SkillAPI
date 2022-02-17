package skillapi.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import skillapi.Application;
import skillapi.PlayerSkills;
import skillapi.Skill;
import skillapi.api.annotation.SkillEvent;
import skillapi.packets.InitSkillPacket;
import skillapi.packets.SkillPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SkillEvent
public final class PlayerEventHandler {
    public Map<UUID, List<String>> knownSkillsBackup = new HashMap<>();
    public Map<UUID, Skill[]> skillBarBackup = new HashMap<>();

    @SubscribeEvent
    public void onConstructing(EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            if (event.entity.getExtendedProperties("SKILLAPI") == null) {
                event.entity.registerExtendedProperties("SKILLAPI", new PlayerSkills((EntityPlayer) event.entity, 20));
            }
        }
    }

    @SubscribeEvent
    public void onSpawn(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityPlayerMP) {
            SkillPacket pkt = new InitSkillPacket(PlayerSkills.get((EntityPlayer) event.entity));
            Application.channels.get(pkt.getChannel()).sendTo(pkt.getPacket(Side.CLIENT), (EntityPlayerMP) event.entity);
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            PlayerSkills sk = PlayerSkills.get((EntityPlayer) event.entityLiving);
            sk.setPrevMana(sk.getMana());
            if (sk.getMana() < 20 && event.entityLiving.ticksExisted % 20 == 0) {
                sk.restoreMana(1);
            }
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            PlayerSkills skills = PlayerSkills.get((EntityPlayer) event.entityLiving);
            skills.resetSkills();
            if (event.entityLiving instanceof EntityPlayerMP) {
                knownSkillsBackup.put(event.entityLiving.getUniqueID(), skills.knownSkills);
                skillBarBackup.put(event.entityLiving.getUniqueID(), skills.skillBar);
            }
        }
    }

    @SubscribeEvent
    public void afterSleepInBed(PlayerSleepInBedEvent event) {
        if (!event.entityPlayer.worldObj.isRemote && !event.entityPlayer.isPlayerSleeping()) {
            if (!event.entityPlayer.worldObj.isDaytime() && event.entityPlayer.isEntityAlive() && event.entityPlayer.worldObj.provider.isSurfaceWorld()) {
                PlayerSkills.get(event.entityPlayer).resetSkills();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        SkillPacket pkt = new InitSkillPacket(PlayerSkills.get(event.player));
        Application.channels.get(pkt.getChannel()).sendTo(pkt.getPacket(Side.CLIENT), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        SkillPacket pkt = new InitSkillPacket(PlayerSkills.get(event.player));
        Application.channels.get(pkt.getChannel()).sendTo(pkt.getPacket(Side.CLIENT), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerSkills.get(event.player).setMana(20);
        if (knownSkillsBackup.containsKey(event.player.getUniqueID())) {
            PlayerSkills.get(event.player).knownSkills = knownSkillsBackup.remove(event.player.getUniqueID());
            PlayerSkills.get(event.player).skillBar = skillBarBackup.remove(event.player.getUniqueID());
        }
        SkillPacket pkt = new InitSkillPacket(PlayerSkills.get(event.player));
        Application.channels.get(pkt.getChannel()).sendTo(pkt.getPacket(Side.CLIENT), (EntityPlayerMP) event.player);
    }
}
