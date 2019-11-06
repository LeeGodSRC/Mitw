package net.development.mitw.queue.module.command.cmds;

import net.development.mitw.queue.module.util.C;
import net.development.mitw.queue.module.util.EzQueueUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player) || !cmd.getName().equalsIgnoreCase("play")) {
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 0) {
            player.sendMessage(C.RED + "Usage: /play <server>");
            return true;
        }
        final String server = args[0];
        EzQueueUtil.sendJoinRequest(player, server);
        return true;
    }
}
