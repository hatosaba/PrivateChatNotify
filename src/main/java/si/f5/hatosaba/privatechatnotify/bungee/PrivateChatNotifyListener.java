package si.f5.hatosaba.privatechatnotify.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Arrays;

public class PrivateChatNotifyListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTabComplete(TabCompleteEvent event) {
        final String commandName = event.getCursor().split(" ")[0];
        String partialPlayerName = event.getCursor().toLowerCase();

        if (Arrays.asList("tell", "msg", "message", "m", "w", "t").contains(commandName)) {

            int lastSpaceIndex = partialPlayerName.lastIndexOf(' ');
            if (lastSpaceIndex >= 0) {
                partialPlayerName = partialPlayerName.substring(lastSpaceIndex + 1);
            }

            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                if (p.getName().toLowerCase().startsWith(partialPlayerName)) {
                    event.getSuggestions().add(p.getName());
                }
            }
        }
    }
}
