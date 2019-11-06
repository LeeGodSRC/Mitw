package net.development.mitw.queue.module;

import net.development.mitw.queue.module.command.cmds.CreateInfoCommand;
import net.development.mitw.queue.module.command.cmds.CreateJoinCommand;
import net.development.mitw.queue.module.signs.QueueInfoSign;
import net.development.mitw.queue.module.signs.QueueJoinSign;
import net.development.mitw.queue.module.util.C;
import net.development.mitw.queue.module.util.EzQueueUtil;
import net.development.mitw.queue.shared.BungeeChannel;
import net.development.mitw.queue.shared.QueueInfo;
import net.development.mitw.queue.shared.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class QueueListener implements Listener, PluginMessageListener
{
    public void onPluginMessageReceived(final String c, final Player p, final byte[] message) {
        final DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        final BungeeChannel channel = BungeeChannel.getChannel(c);
        if (channel == null) {
            return;
        }
        try {
            final String input = in.readUTF();
            final String[] args = input.split(",");
            switch (channel) {
                case CREATE_QUEUE: {
                    final Player player = Bukkit.getPlayer(args[0]);
                    final String signType = args[1];
                    final String queue = args[2];
                    final Location loc = EzQueueUtil.stringToLoc(args[3]);
                    if (player == null) {
                        return;
                    }
                    if (QueueInfoSign.getSign(loc) != null || QueueInfoSign.getSign(loc) != null) {
                        player.sendMessage(C.RED + "There is already a queue sign at this location!");
                        return;
                    }
                    if (queue.equalsIgnoreCase("null")) {
                        player.sendMessage(C.RED + "Could not find queue with given name! Are you sure it's in your EzQueueBungee config?");
                        return;
                    }
                    if (signType.equalsIgnoreCase("join")) {
                        player.sendMessage(C.GREEN + "Created " + C.YELLOW + "join " + C.GREEN + "sign for " + C.YELLOW + queue + C.GREEN + "!");
                        new QueueJoinSign(queue, loc);
                        QueueManager.getInstance().getConfigUtil().saveSigns();
                        return;
                    }
                    if (signType.equalsIgnoreCase("info")) {
                        player.sendMessage(C.GREEN + "Created " + C.YELLOW + "info " + C.GREEN + "sign for " + C.YELLOW + queue + C.GREEN + "!");
                        new QueueInfoSign(queue, loc);
                        QueueManager.getInstance().getConfigUtil().saveSigns();
                        return;
                    }
                    break;
                }
                case UPDATE_POSITIONS: {
                    final QueueInfo info = QueueInfo.deserialize(input);
                    QueueJoinSign.getSigns(info.getQueue()).forEach(s -> s.update(info.getOnline(), info.getMax()));
                    for (final Rank rank : Rank.getRanks()) {
                        QueueInfoSign.getSigns(info.getQueue()).forEach(s -> s.update(rank.getName(), info.getRankCount(rank)));
                    }
                    break;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @EventHandler
    public void onSignClick(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();
        if (block == null || !(block.getState() instanceof Sign)) {
            return;
        }
        final Location loc = block.getLocation();
        if (CreateJoinCommand.isCreating(player)) {
            final String queue = CreateJoinCommand.getCreating(player);
            EzQueueUtil.sendSignCreateRequest(player, "join", queue, loc);
            CreateJoinCommand.getCreating().remove(player.getUniqueId());
            return;
        }
        if (CreateInfoCommand.isCreating(player)) {
            final String queue = CreateInfoCommand.getCreating(player);
            EzQueueUtil.sendSignCreateRequest(player, "info", queue, loc);
            CreateInfoCommand.getCreating().remove(player.getUniqueId());
            return;
        }
        if (QueueJoinSign.isSign(loc)) {
            final String queue = QueueJoinSign.getSign(loc).getQueue();
            EzQueueUtil.sendJoinRequest(player, queue);
        }
        if (QueueInfoSign.isSign(loc)) {
            player.sendMessage(QueueManager.getInstance().getConfigUtil().getInfoMsg());
        }
    }
}
