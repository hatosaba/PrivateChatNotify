package si.f5.hatosaba.privatechatnotify.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import si.f5.hatosaba.privatechatnotify.bungee.PrivateChatNotify;

public class ReplyCommand extends TellCommand {

    public ReplyCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        String recieverName = PrivateChatNotify.getInstance().getHistory(sender.getName());

        // 引数が無いときは、現在の会話相手を表示して終了する。
        if (args.length == 0) {
            if (recieverName == null) {
                sendMessage(sender, ChatColor.RED + "Nobody has messaged you in the everytime");
                return;
            }
        }

        ProxiedPlayer reciever = ProxyServer.getInstance().getPlayer(
                PrivateChatNotify.getInstance().getHistory(sender.getName()));

        if (reciever == null) {
            sendMessage(sender,  ChatColor.RED + " is offline");
            return;
        }

        // 送信メッセージの作成
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            str.append(args[i] + " ");
        }
        String message = str.toString().trim();

        PrivateChatNotify.getInstance().sendData(reciever, message);
    }
}

