package com.github.junzzzz.skillapi.event;

import com.github.junzzzz.skillapi.api.annotation.SkillEvent;
import com.github.junzzzz.skillapi.client.SkillHud;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
