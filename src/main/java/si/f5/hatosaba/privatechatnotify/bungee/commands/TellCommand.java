package si.f5.hatosaba.privatechatnotify.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import si.f5.hatosaba.privatechatnotify.bungee.PrivateChatNotify;

public class TellCommand extends Command {

    public TellCommand(String commandName) {
        super(commandName);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sendMessage(sender, ChatColor.RED +
                    "Invalid usage! Use： /" + this.getName() + " (player) (message)");
            return;
        }

        if (args[0].equals(sender.getName())) {
            sendMessage(sender, ChatColor.RED +
                    "You cannot message this player.");
            return;
        }

        ProxiedPlayer reciever = ProxyServer.getInstance().getPlayer(args[0]);
        if (reciever == null) {
            sendMessage(sender, ChatColor.RED +
                    "Can't find a player by the name of '" + args[0] + "'");
            return;
        }

        StringBuilder str = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            str.append(args[i] + " ");
        }
        String message = str.toString().trim();

        PrivateChatNotify.getInstance().putHistory(reciever.getName(), sender.getName());

        // 送信
        PrivateChatNotify.getInstance().sendData(reciever, message);
    }

    public void sendMessage(CommandSender reciever, String message) {
        if (message == null) return;
        reciever.sendMessage(TextComponent.fromLegacyText(message));
    }
}
