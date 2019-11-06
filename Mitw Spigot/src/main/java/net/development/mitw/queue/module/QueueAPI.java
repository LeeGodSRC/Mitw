package net.development.mitw.queue.module;

import net.development.mitw.queue.module.util.EzQueueUtil;
import net.development.mitw.queue.shared.QueueInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class QueueAPI
{
    public static int getPosition(final String name) {
        final Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return -1;
        }
        return QueueInfo.getPosition(player.getName());
    }
    
    public static int getPosition(final UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return -1;
        }
        return QueueInfo.getPosition(player.getName());
    }
    
    public static int getPosition(final Player player) {
        return getPosition(player.getName());
    }
    
    public static Map<String, Integer> getPlayersInQueue(final String queue) {
        final QueueInfo info = QueueInfo.getQueueInfo(queue);
        if (info == null) {
            return null;
        }
        return info.getPlayersInQueue();
    }
    
    public static String getQueue(final String name) {
        return QueueInfo.getQueue(name);
    }
    
    public static String getQueue(final UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
        return QueueInfo.getQueue(player.getName());
    }
    
    public static String getQueue(final Player player) {
        return getQueue(player.getName());
    }
    
    public static int getQueueSize(final String queue) {
        final QueueInfo info = QueueInfo.getQueueInfo(queue);
        if (info == null) {
            return -1;
        }
        return info.getSize();
    }
    
    public static void addToQueue(final Player player, final String queue) {
        EzQueueUtil.sendJoinRequest(player, queue);
    }
}
