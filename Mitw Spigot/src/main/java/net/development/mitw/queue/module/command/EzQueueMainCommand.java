package net.development.mitw.queue.module.command;

import net.development.mitw.queue.module.command.cmds.CreateInfoCommand;
import net.development.mitw.queue.module.command.cmds.CreateJoinCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EzQueueMainCommand implements CommandExecutor
{
    public EzQueueMainCommand() {
        new CreateInfoCommand();
        new CreateJoinCommand();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!cmd.getName().equalsIgnoreCase("ezqueue")) {
            return true;
        }
        if (args.length == 0) {
            this.sendHelp(sender);
            return true;
        }
        final EzQueueCommand gc = EzQueueCommand.getCommand(args[0]);
        if (gc == null) {
            this.sendHelp(sender);
            return true;
        }
        gc.run(sender, label, cmd, args);
        return true;
    }
    
    private void sendHelp(final CommandSender sender) {
        for (final EzQueueCommand qc : EzQueueCommand.getCommands()) {
            qc.sendFullUsage(sender);
        }
    }
}
