package io.github.junzzzz.skillapi.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.junzzzz.skillapi.api.annotation.SkillEvent;
import io.github.junzzzz.skillapi.client.SkillHud;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
@SkillEvent
public class SkillClientEvents {
    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.FOOD) {
            SkillHud.drawHud(event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
        }
    }
}
