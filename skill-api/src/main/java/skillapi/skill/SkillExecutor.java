package skillapi.skill;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import skillapi.api.annotation.SkillEvent;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Jun
 */
@SkillEvent
public final class SkillExecutor {
    private static Deque<TriggerUnit> triggerQueue = new ArrayDeque<>(64);
    private static Deque<TriggerUnit> reuseQueue = new ArrayDeque<>(64);

    public static void init() {
        triggerQueue = new ArrayDeque<>(64);
        reuseQueue = new ArrayDeque<>(64);
    }

    public static void execute(AbstractSkill skill, EntityPlayer player, EntityLivingBase target) {
        if (skill == null || player == null) {
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
        unit.target = target;
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
        EntityLivingBase target;
        AbstractSkill skill;

        public void unleash() {
            skill.unleash(player, target);
        }
    }
}
