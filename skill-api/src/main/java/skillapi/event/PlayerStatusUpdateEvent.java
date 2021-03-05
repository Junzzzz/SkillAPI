package skillapi.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import skillapi.api.annotation.SkillEvent;
import skillapi.event.base.BaseSkillEvent;

/**
 * @author Jun
 * @date 2020/8/22.
 */
@SkillEvent
public final class PlayerStatusUpdateEvent extends BaseSkillEvent<LivingUpdateEvent> {
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
