package io.github.junzzzz.skillapi.event;

import io.github.junzzzz.skillapi.api.annotation.SkillEvent;
import io.github.junzzzz.skillapi.event.base.AbstractSkillEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

/**
 * @author Jun
 * @date 2020/8/22.
 */
@SkillEvent
public final class PlayerStatusUpdateEvent extends AbstractSkillEvent<LivingUpdateEvent> {
    @Override
    protected void onServer(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
//            System.out.println("server");
        }
    }

    @Override
    protected void onClient(LivingUpdateEvent event) {
        if (event.entity instanceof EntityPlayer) {
//            System.out.println("client");
        }
    }
}
