package skillapi.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

/**
 * @author Jun
 */
public final class Message {
    public static void send(EntityPlayer player, String message) {
        player.addChatComponentMessage(new ChatComponentText(message));
    }

    public static void sendTranslation(EntityPlayer player, String translationKey, Object... params) {
        player.addChatComponentMessage(new ChatComponentTranslation(translationKey, params));
    }
}
