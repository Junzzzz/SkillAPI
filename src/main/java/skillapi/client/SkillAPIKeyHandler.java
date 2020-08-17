package skillapi.client;

import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Booleans;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import skillapi.PlayerSkills;
import skillapi.SkillAPI;
import skillapi.packets.SkillPacket;
import skillapi.packets.TriggerSkillPacket;

public class SkillAPIKeyHandler {
    public static final SkillAPIKeyHandler INSTANCE = new SkillAPIKeyHandler();
    public KeyBinding[] keyBindings = new KeyBinding[0];
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
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (Minecraft.getMinecraft().currentScreen == null) {
                for (int i = 0; i < SkillAPIClientProxy.skillKeyBindings.length; i++) {
                    if (SkillAPIClientProxy.skillKeyBindings[i] == kb && PlayerSkills.get(Minecraft.getMinecraft().thePlayer).skillBar[i] != null) {
                        SkillPacket pkt = new TriggerSkillPacket(Minecraft.getMinecraft().thePlayer.getEntityId(), i, PlayerSkills.get(Minecraft.getMinecraft().thePlayer).skillBar[i].getName());
                        SkillAPI.channels.get(pkt.getChannel()).sendToServer(pkt.getPacket(Side.SERVER));
                        return;
                    }
                }
            }
            if (kb == SkillAPIClientProxy.skillGuiKeyBinding) {
                if (Minecraft.getMinecraft().currentScreen == null) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiKnownSkills(PlayerSkills.get(Minecraft.getMinecraft().thePlayer)));
                } else if (Minecraft.getMinecraft().currentScreen instanceof GuiKnownSkills) {
                    Minecraft.getMinecraft().thePlayer.closeScreen();
                }
            }
        }
    }

    void addKeyBinding(KeyBinding binding, boolean repeats) {
        this.keyBindings = ObjectArrays.concat(this.keyBindings, binding);
        this.repeatings = Booleans.concat(this.repeatings, new boolean[]{repeats});
        this.active = new boolean[this.keyBindings.length];
    }

    void setKeyBinding(int i, boolean repeats) {
        this.repeatings[i + 1] = repeats;
    }
}
