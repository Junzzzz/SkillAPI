package skillapi.client;

import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Booleans;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import skillapi.Application;
import skillapi.PlayerSkills;
import skillapi.api.gui.base.GuiApi;
import skillapi.client.gui.KnownSkillsGui;
import skillapi.packets.SkillPacket;
import skillapi.packets.TriggerSkillPacket;

/**
 * 按键交互（技能面板和技能释放）
 *
 * @author GotoLink, Junzzzz
 */
@SideOnly(Side.CLIENT)
public class SkillAPIKeyHandler {
    public static final SkillAPIKeyHandler INSTANCE = new SkillAPIKeyHandler();
    public KeyBinding[] keyBindings = new KeyBinding[0];
    /**
     * 是否可以按住后持续触发
     */
    private boolean[] repeatings = new boolean[0];
    private boolean[] active = new boolean[0];

    private SkillAPIKeyHandler() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void keyDown(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (int idx = 0; idx < keyBindings.length; idx++) {
                if (keyBindings[idx].getIsKeyPressed()) {
                    if (!active[idx] || repeatings[idx]) {
                        fireKey(keyBindings[idx]);
                        active[idx] = true;
                    }
                } else {
                    active[idx] = false;
                }
            }
        }
    }

    private void fireKey(KeyBinding kb) {
        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (player != null) {
            // 释放技能
            if (Minecraft.getMinecraft().currentScreen == null) {
                for (int i = 0; i < SkillAPIClientProxy.skillKeyBindings.length; i++) {
                    if (SkillAPIClientProxy.skillKeyBindings[i] == kb && PlayerSkills.get(player).skillBar[i] != null) {
                        SkillPacket pkt = new TriggerSkillPacket(player.getEntityId(), i, PlayerSkills.get(player).skillBar[i].getName());
                        Application.channels.get(pkt.getChannel()).sendToServer(pkt.getPacket(Side.SERVER));
                        return;
                    }
                }
            }
            // 打开技能面板
            if (kb == SkillAPIClientProxy.skillGuiKeyBinding) {
                if (Minecraft.getMinecraft().currentScreen == null) {
//                    Minecraft.getMinecraft().displayGuiScreen(new GuiKnownSkills(PlayerSkills.get(player)));
                    GuiApi.displayGui(new KnownSkillsGui());
                }
            }
        }
    }

    void addKeyBinding(KeyBinding binding, boolean repeat) {
        this.keyBindings = ObjectArrays.concat(this.keyBindings, binding);
        this.repeatings = Booleans.concat(this.repeatings, new boolean[]{repeat});
        this.active = new boolean[this.keyBindings.length];
    }

    void setKeyBinding(int i, boolean repeats) {
        this.repeatings[i + 1] = repeats;
    }
}
