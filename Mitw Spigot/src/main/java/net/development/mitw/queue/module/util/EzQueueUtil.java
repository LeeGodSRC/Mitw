package net.development.mitw.queue.module.util;

import net.development.mitw.Mitw;
import net.development.mitw.queue.module.QueueManager;
import net.development.mitw.queue.shared.BungeeChannel;
import net.development.mitw.queue.shared.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EzQueueUtil
{
    public static void sendJoinRequest(final Player player, final String queue) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);
        try {
            final Rank rank = getRank(player);
            out.writeUTF(player.getName() + "," + queue + "," + ((rank == null) ? null : rank.getPermission()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Mitw.getInstance(), BungeeChannel.JOIN_QUEUE.getChannel(), b.toByteArray());
    }
    
    public static void sendSignCreateRequest(final Player player, final String signType, final String queue, final Location loc) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(player.getName() + "," + signType + "," + queue + "," + locToString(loc));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage((Plugin) QueueManager.getInstance(), BungeeChannel.CREATE_QUEUE.getChannel(), b.toByteArray());
    }
    
    public static Rank getRank(final Player player) {
        Rank playerRank = null;
        for (final Rank rank : Rank.getRanks()) {
            if (player.hasPermission(rank.getPermission())) {
                if (playerRank == null) {
                    playerRank = rank;
                }
                else {
                    if (rank.getPriority() >= playerRank.getPriority()) {
                        continue;
                    }
                    playerRank = rank;
                }
            }
        }
        return playerRank;
    }
    
    public static String locToString(final Location loc) {
        return loc.getWorld().getName() + "@" + loc.getBlockX() + "@" + loc.getBlockY() + "@" + loc.getBlockZ();
    }
    
    public static Location stringToLoc(final String loc) {
        final String[] args = loc.split("@");
        return new Location(Bukkit.getWorld(args[0]), (double)Integer.valueOf(args[1]), (double)Integer.valueOf(args[2]), (double)Integer.valueOf(args[3]));
    }
}
