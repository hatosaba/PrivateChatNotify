package si.f5.hatosaba.privatechatnotify.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import si.f5.hatosaba.privatechatnotify.bungee.commands.ReplyCommand;
import si.f5.hatosaba.privatechatnotify.bungee.commands.TellCommand;
import si.f5.hatosaba.privatechatnotify.shared.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class PrivateChatNotify extends Plugin {

    private static PrivateChatNotify instance;

    private HashMap<String, String> history;

    @Override
    public void onEnable() {
        instance = this;
        // コマンド登録
        for (String commandName : new String[]{"tell", "msg", "message", "m", "w", "t"})
            getProxy().getPluginManager().registerCommand(this, new TellCommand(commandName));

        for (String commandName : new String[]{"reply", "r"})
            getProxy().getPluginManager().registerCommand(this, new ReplyCommand(commandName));

        getProxy().getPluginManager().registerListener(this, new PrivateChatNotifyListener());

    }

    @Override
    public void onDisable() {}

    public static PrivateChatNotify getInstance() {
        return instance;
    }

    public void sendData(ProxiedPlayer toPlayer, String message) {
        ByteArrayDataOutput response = ByteStreams.newDataOutput();
        response.writeUTF(Constants.PROXY_SUB_CHANNEL_NAME);
        response.writeUTF(toPlayer.getUniqueId().toString());

        ByteArrayOutputStream msgBody = new ByteArrayOutputStream();
        DataOutputStream msgBodyData = new DataOutputStream(msgBody);
        try {
            msgBodyData.writeUTF(message);
        } catch(IOException exception) {
            exception.printStackTrace();
        }

        toPlayer.getServer().getInfo().sendData(Constants.PROXY_CHANNEL_NAME, response.toByteArray());
    }

    public void putHistory(String reciever, String sender) {
        history.put(reciever, sender);
    }

    public String getHistory(String reciever) {
        return history.get(reciever);
    }

}
