package mitw.bungee.queue.module.command;

import mitw.bungee.queue.module.util.C;
import mitw.bungee.queue.module.util.EzQueueUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public abstract class PlayerOnlyCommand extends Command
{
    public PlayerOnlyCommand(final String name) {
        super(name);
    }
    
    public PlayerOnlyCommand(final String name, final String permission, final String[] aliases) {
        super(name, permission, aliases);
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (!this.isPlayer(sender)) {
            sender.sendMessage((BaseComponent) EzQueueUtil.toComponent(String.valueOf(C.RED) + "This is a player only command!"));
            return;
        }
        this.onExecute(this.asPlayer(sender), args);
    }
    
    public abstract void onExecute(final ProxiedPlayer p0, final String[] p1);
    
    public boolean isPlayer(final CommandSender sender) {
        return sender instanceof ProxiedPlayer;
    }
    
    public ProxiedPlayer asPlayer(final CommandSender sender) {
        return (ProxiedPlayer)sender;
    }
}
