package net.development.mitw.queue.module.command.cmds;

import net.development.mitw.queue.module.command.EzQueueCommand;
import net.development.mitw.queue.module.signs.QueueInfoSign;
import net.development.mitw.queue.module.signs.QueueJoinSign;
import net.development.mitw.queue.module.util.C;
import net.development.mitw.queue.module.util.EzQueueUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CreateInfoCommand extends EzQueueCommand
{
    private static Map<UUID, String> creating;
    
    public CreateInfoCommand() {
        super("createinfosign", new String[0]);
        this.setUsage("/ezqueue createinfosign <server>");
        this.setPlayerOnly(true);
        this.setPermission("ezq.create");
        this.setDescription("Create an EzQueue info sign");
    }
    
    @Override
    public void run(final CommandSender sender, final String[] args) {
        final Player player = this.asPlayer(sender);
        if (args.length < 1) {
            this.sendFullUsage(sender);
            return;
        }
        if (Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.6")) {
            CreateInfoCommand.creating.put(player.getUniqueId(), args[0]);
            player.sendMessage(C.GREEN + "Right click a sign you wish to make a queue info sign.");
            return;
        }
        final Block block = player.getTargetBlock((Set)null, 100);
        if (!(block.getState() instanceof Sign)) {
            player.sendMessage(C.RED + "You must be looking at a sign!");
            return;
        }
        final Location loc = block.getLocation();
        if (QueueJoinSign.getSign(loc) != null || QueueInfoSign.getSign(loc) != null) {
            player.sendMessage(C.RED + "There is already a queue sign at this location.");
            return;
        }
        EzQueueUtil.sendSignCreateRequest(player, "info", args[0], loc);
    }
    
    public static String getCreating(final Player player) {
        return CreateInfoCommand.creating.get(player.getUniqueId());
    }
    
    public static boolean isCreating(final Player player) {
        return getCreating(player) != null;
    }
    
    public static Map<UUID, String> getCreating() {
        return CreateInfoCommand.creating;
    }
    
    static {
        CreateInfoCommand.creating = new HashMap<UUID, String>();
    }
}
