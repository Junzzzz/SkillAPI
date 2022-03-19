package skillapi.skill;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEvent;
import skillapi.common.SkillLog;
import skillapi.packet.ClientSkillUnleashPacket;
import skillapi.packet.base.Packet;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Jun
 */
@SkillEvent
public final class SkillExecutor {
    private static Deque<TriggerUnit> triggerQueue = new ArrayDeque<>(64);
    private static Deque<TriggerUnit> reuseQueue = new ArrayDeque<>(64);

    private static boolean STOP_RECEIVE = false;

    public static void init() {
        triggerQueue = new ArrayDeque<>(64);
        reuseQueue = new ArrayDeque<>(64);
    }

    public static synchronized void resume() {
        STOP_RECEIVE = false;
    }

    public static synchronized void stop() {
        STOP_RECEIVE = true;
        triggerQueue.clear();
    }

    public static void execute(AbstractSkill skill, EntityPlayer player, SkillExtraInfo extraInfo) {
        if (skill == null || player == null || STOP_RECEIVE) {
            return;
        }
        TriggerUnit unit;
        if (!reuseQueue.isEmpty()) {
            unit = reuseQueue.poll();
        } else {
            unit = new TriggerUnit();
        }
        unit.skill = skill;
        unit.player = player;
        unit.extraInfo = extraInfo;
        triggerQueue.offer(unit);
    }

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        while (!triggerQueue.isEmpty()) {
            TriggerUnit unit = triggerQueue.poll();
            unit.unleash();
            // Reduce GC
            reuseQueue.offer(unit);
        }
    }

    private static class TriggerUnit {
        EntityPlayer player;
        AbstractSkill skill;
        SkillExtraInfo extraInfo;

        public void unleash() {
            if (player != null && player.isEntityAlive() && skill.canUnleash(player, extraInfo)) {
                try {
                    if (skill.unleash(player, extraInfo)) {
                        skill.afterUnleash(player, extraInfo);

                        // Client
                        Packet.send(new ClientSkillUnleashPacket(skill), player);
                    }
                } catch (Exception e) {
                    String profileName = Skills.getCurrentProfileInfo().getName();
                    SkillLog.error(e, "Unleash skill failed! Player: %s, Profile: %s, Skill: %s",
                            player.getCommandSenderName(), profileName, skill.getLocalizedName());
                }
            }
        }
    }
}
