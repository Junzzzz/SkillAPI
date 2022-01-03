package skillapi.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import skillapi.Application;
import skillapi.api.annotation.SkillEvent;
import skillapi.skill.PlayerSkills;

/**
 * @author Jun
 */
@SkillEvent
public class SkillCommonEvents {
    @SubscribeEvent
    public void onConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            event.entity.registerExtendedProperties(Application.MOD_ID, new PlayerSkills());
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof AbstractClientPlayer) {
            // About 1 second
            if (event.entityLiving.ticksExisted % 20 == 0) {
                PlayerSkills skill = PlayerSkills.get((AbstractClientPlayer) event.entityLiving);
            }
        }
    }

}
