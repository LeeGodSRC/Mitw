package mitw.bungee.queue.module;

import mitw.bungee.Mitw;
import mitw.bungee.queue.module.queue.Queue;
import mitw.bungee.queue.module.util.EzQueueUtil;
import mitw.bungee.queue.shared.BungeeChannel;
import mitw.bungee.queue.shared.Rank;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class QueueListener implements Listener
{
    private QueueManager plugin;
    
    @EventHandler
    public void onPluginMessage(final PluginMessageEvent event) throws IOException {
        final BungeeChannel channel = BungeeChannel.getChannel(event.getTag());
        if (channel == null) {
            return;
        }
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        final String input = in.readUTF();
        final String[] args = input.split(",");
        switch (channel) {
            case CREATE_QUEUE: {
                final ProxiedPlayer player = Mitw.INSTANCE.getProxy().getPlayer(args[0]);
                if (player == null) {
                    return;
                }
                final String signType = args[1];
                final Queue queue = Queue.getQueue(args[2]);
                final String loc = args[3];
                if (queue == null) {
                    player.sendMessage((BaseComponent) QueueMessages.getMsg().getAvailableQueuesMessage());
                    return;
                }
                EzQueueUtil.sendCreateResponse(player, signType, queue.getName(), loc);
                break;
            }
            case JOIN_QUEUE: {
                final ProxiedPlayer player = Mitw.INSTANCE.getProxy().getPlayer(args[0]);
                final Queue queue2 = Queue.getQueue(args[1]);
                final Rank rank = Rank.getRank(args[2]);
                if (player == null) {
                    return;
                }
                if (queue2 == null) {
                    final ServerInfo info = Mitw.INSTANCE.getProxy().getServerInfo(args[1]);
                    if (info != null) {
                        player.connect(info);
                    }
                    else {
                        player.sendMessage((BaseComponent) QueueMessages.getMsg().getAvailableQueuesMessage());
                    }
                    return;
                }
                queue2.add(player, rank);
                break;
            }
        }
    }
    
    @EventHandler
    public void onDisconnect(final PlayerDisconnectEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final Queue queue = Queue.getQueue(player);
        if (queue != null) {
            queue.remove(player);
        }
    }
    
    public QueueListener(final QueueManager plugin) {
        this.plugin = plugin;
    }
}
