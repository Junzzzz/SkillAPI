package skillapi.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import skillapi.api.annotation.SkillEvent;
import skillapi.utils.ClientUtils;

/**
 * @author Jun
 * @date 2020/9/21.
 */
@SkillEvent
public class PlayerRenderEvent {
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        if (!ClientUtils.isInGame()) {
            return;
        }

        final Entity entity = ClientUtils.getPointedLivingEntity(10.0D, event.renderTickTime);
        if (entity != null) {

        }
    }

    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.FOOD) {
            return;
        }
        final MovingObjectPosition objectMouseOver = Minecraft.getMinecraft().objectMouseOver;

        if (objectMouseOver.entityHit != null) {
            objectMouseOver.entityHit.hitByEntity(Minecraft.getMinecraft().thePlayer);
            System.out.println("hit2");
        }

    }
}
