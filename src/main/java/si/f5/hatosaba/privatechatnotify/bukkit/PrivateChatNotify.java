package si.f5.hatosaba.privatechatnotify.bukkit;

import com.github.ucchyocean.lc3.LunaChat;
import com.github.ucchyocean.lc3.japanize.JapanizeType;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import si.f5.hatosaba.privatechatnotify.shared.utils.Constants;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public final class PrivateChatNotify extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (Bukkit.getPluginManager().isPluginEnabled("LunaChat")) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, Constants.PROXY_CHANNEL_NAME);
            getServer().getMessenger().registerIncomingPluginChannel(this, Constants.PROXY_CHANNEL_NAME, new ChatNotifyHandler());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public class ChatNotifyHandler implements PluginMessageListener {

        @Override
        public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] data) {
            if (!channel.equals(Constants.PROXY_CHANNEL_NAME))
                return;

            ByteArrayDataInput response = ByteStreams.newDataInput(data);
            String subChannelName = response.readUTF();

            if (!subChannelName.equals(Constants.PROXY_SUB_CHANNEL_NAME))
                return;

            String toPlayerName = response.readUTF();

            short bodyLength = response.readShort();
            byte[] msgBody = new byte[bodyLength];
            response.readFully(msgBody);

            DataInputStream msgBodyData = new DataInputStream(new ByteArrayInputStream(msgBody));

            try {
                String message = msgBodyData.readUTF();
                sendPersonalMessage(toPlayerName, message);
            } catch (IOException | IllegalArgumentException exception) {
                exception.printStackTrace();
            }

        }

        public void sendPersonalMessage(String toPlayerName, String message) {
            final Player toPlayer = Bukkit.getPlayer(toPlayerName);

            if (toPlayer != null) {
                if (LunaChat.getAPI().isPlayerJapanize(toPlayerName)) {
                    toPlayer.sendMessage(LunaChat.getAPI().japanize(message, JapanizeType.GOOGLE_IME));
                } else {
                    toPlayer.sendMessage(message);
                }
            }

            toPlayer.playSound(toPlayer.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
        }
    }
}
