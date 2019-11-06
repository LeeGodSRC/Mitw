package mitw.bungee.queue.module.util;

import mitw.bungee.Mitw;
import mitw.bungee.queue.module.LobbyServer;
import mitw.bungee.queue.module.queue.Queue;
import mitw.bungee.queue.module.queue.QueuedPlayer;
import mitw.bungee.queue.shared.BungeeChannel;
import mitw.bungee.queue.shared.QueueInfo;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EzQueueUtil
{
    public static void sendCreateResponse(final ProxiedPlayer player, final String signType, final String queue, final String loc) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(stream);
        try {
            final String toWrite = String.valueOf(player.getName()) + "," + signType + "," + queue + "," + loc;
            out.writeUTF(toWrite);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        player.getServer().getInfo().sendData(BungeeChannel.CREATE_QUEUE.getChannel(), stream.toByteArray());
    }
    
    public static void sendQueueInfo() {
        for (final LobbyServer lobby : LobbyServer.getLobbies()) {
            if (lobby.getInfo().getPlayers().isEmpty()) {
                continue;
            }
            for (final Queue queue : new ArrayList<Queue>(Queue.getQueues())) {
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                final DataOutputStream out = new DataOutputStream(stream);
                QueueInfo info = QueueInfo.getQueueInfo(queue.getName());
                if (info == null) {
                    info = new QueueInfo(queue.getName());
                }
                info.setInQueueString(queuedToString(queue));
                info.setOnline(queue.getInfo().getPlayers().size());
                info.setMax(queue.getLimit());
                try {
                    out.writeUTF(info.serialize());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                lobby.getInfo().sendData(BungeeChannel.UPDATE_POSITIONS.getChannel(), stream.toByteArray());
            }
        }
    }
    
    public static void sendPositionMessage() {
        Queue.getQueues().forEach(q -> q.sendPositionMessage(0, false));
    }
    
    public static String getQueues() {
        String queueNames = "";
        for (final Queue queue : Queue.getQueues()) {
            if (!queueNames.isEmpty()) {
                queueNames = String.valueOf(queueNames) + ", ";
            }
            queueNames = String.valueOf(queueNames) + queue.getInfo().getName();
        }
        return queueNames;
    }
    
    public static String getServers() {
        String serverNames = "";
        for (final String server : Mitw.INSTANCE.getProxy().getServers().keySet()) {
            if (!serverNames.isEmpty()) {
                serverNames = String.valueOf(serverNames) + ", ";
            }
            serverNames = String.valueOf(serverNames) + server;
        }
        return serverNames;
    }
    
    private static String queuedToString(final Queue queue) {
        String str = "";
        for (final QueuedPlayer qp : queue.getQueued()) {
            if (!str.isEmpty()) {
                str = String.valueOf(str) + "~";
            }
            str = String.valueOf(str) + qp.getPlayer().getName() + "=" + qp.getRank() + "=" + queue.getPosition(qp.getPlayer());
        }
        return str;
    }
    
    public static TextComponent toComponent(final String msg) {
        return new TextComponent(msg);
    }
}
