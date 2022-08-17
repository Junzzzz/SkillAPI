package io.github.junzzzz.skillapi.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.junzzzz.skillapi.api.annotation.SkillEvent;
import io.github.junzzzz.skillapi.api.gui.base.GuiApi;
import io.github.junzzzz.skillapi.client.gui.KnownSkillsGui;
import io.github.junzzzz.skillapi.common.Message;
import io.github.junzzzz.skillapi.packet.base.Packet;
import io.github.junzzzz.skillapi.skill.*;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * @author Jun
 */
@SideOnly(Side.CLIENT)
@SkillEvent(Side.CLIENT)
public class SkillClient {
    public static PlayerSkills SKILL;
    public static boolean drawHud = false;

    public static KeyBinding showSkillGuiKey;
    public static KeyBinding[] unleashSkillKey = new KeyBinding[5];

    private static final String KEY_GUI = "K";
    private static final String[] KEY_SKILL = {"Z", "X", "C", "V", "B"};

    public static final String GROUP_GUI = "key.categories.gui";
    public static final String GROUP_GAMEPLAY = "key.categories.gameplay";

    private static final SkillExtraInfo SKILL_EXTRA_INFO = new SkillExtraInfo();

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
        if (SKILL.getMana() < skill.getMana()) {
            Message.send(SKILL.getPlayer(), "[客户端] 魔力不足: " + SKILL.getMana() + "/" + skill.getMana());
            return;
        }
        if (!cooldown.isCooledDown()) {
            Message.send(SKILL.getPlayer(), "[客户端] 技能未冷却: " + cooldown);
            return;
        }

        // Reuse
        SKILL_EXTRA_INFO.reset();

        if (skill.clientBeforeUnleash(SKILL.getPlayer(), SKILL_EXTRA_INFO)) {
            Packet.sendToServer(new PlayerUnleashSkillPacket(index, SKILL_EXTRA_INFO));
            SKILL.consumeMana(skill.getMana());
            cooldown.setCooling();
            skill.clientUnleash(SKILL.getPlayer(), SKILL_EXTRA_INFO.copy());
        } else {
            // TODO clear
            Message.send(SKILL.getPlayer(), "[客户端] 释放条件不足");
        }
    }
}
