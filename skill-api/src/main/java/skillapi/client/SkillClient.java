package skillapi.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import skillapi.api.annotation.SkillEvent;
import skillapi.api.gui.base.GuiApi;
import skillapi.client.gui.KnownSkillsGui;
import skillapi.packet.PlayerUnleashSkillPacket;
import skillapi.packet.base.Packet;
import skillapi.skill.AbstractSkill;
import skillapi.skill.Cooldown;
import skillapi.skill.PlayerSkills;
import skillapi.utils.ClientUtils;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
@SkillEvent(Side.CLIENT)
public class SkillClient {
    public static PlayerSkills SKILL;

    public static KeyBinding showSkillGuiKey;
    public static KeyBinding[] unleashSkillKey = new KeyBinding[5];

    private static final String KEY_GUI = "K";
    private static final String[] KEY_SKILL = {"Z", "X", "C", "V", "B"};

    public static final String GROUP_GUI = "key.categories.gui";
    public static final String GROUP_GAMEPLAY = "key.categories.gameplay";

    public static void init() {
        showSkillGuiKey = new KeyBinding("key.skillGui", Keyboard.getKeyIndex(KEY_GUI), GROUP_GUI);
        ClientRegistry.registerKeyBinding(showSkillGuiKey);
        for (int i = 0; i < KEY_SKILL.length; i++) {
            String key = "key.skill" + (i + 1);
            unleashSkillKey[i] = new KeyBinding(key, Keyboard.getKeyIndex(KEY_SKILL[i]), GROUP_GAMEPLAY);
            ClientRegistry.registerKeyBinding(unleashSkillKey[i]);
        }
    }

    public static void initPlayerSkill(PlayerSkills properties) {
        SKILL = properties;
    }

    @SubscribeEvent
    public void keyDown(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (showSkillGuiKey.isPressed()) {
                // Open Gui
                openSkillGui();
            } else {
                // Unleash skill
                for (int i = 0; i < unleashSkillKey.length; i++) {
                    if (unleashSkillKey[i].isPressed() && !GuiApi.isDisplaying()) {
                        unleashSkill(i);
                    }
                }
            }
        }
    }

    private void openSkillGui() {
        if (!GuiApi.isDisplaying()) {
            GuiApi.displayGui(new KnownSkillsGui());
        }
    }

    private void unleashSkill(int index) {
        AbstractSkill skill = SKILL.getSkill(index);
        Cooldown cooldown = SKILL.getSkillCooldown(index);
        if (skill == null || cooldown == null) {
            return;
        }
        if (SKILL.getMana() >= skill.getMana() && cooldown.isCooledDown()) {
//            PacketHandler.sendToServer(new WorldTimePacket());
            // TODO distance
            EntityLivingBase target = ClientUtils.getPointedLivingEntity(10.0D);
            Packet.sendToServer(new PlayerUnleashSkillPacket(index, target));
            SKILL.consumeMana(skill.getMana());
        }
    }
}
