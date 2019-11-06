package net.development.mitw.queue.module.command;

import net.development.mitw.queue.module.util.C;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class EzQueueCommand implements IEzQueueCommand
{
    private static List<EzQueueCommand> commands;
    private String name;
    private List<String> aliases;
    private String permission;
    private String description;
    private String usage;
    private boolean playerOnly;
    
    public EzQueueCommand(final String name, final String... aliases) {
        this.aliases = new ArrayList<String>();
        this.playerOnly = false;
        this.name = name;
        this.aliases = Arrays.asList(aliases);
        EzQueueCommand.commands.add(this);
    }
    
    public static EzQueueCommand getCommand(final String cmd) {
        return EzQueueCommand.commands.stream().filter(c -> c.getName().equalsIgnoreCase(cmd) || c.getAliases().contains(cmd.toLowerCase())).findAny().orElse(null);
    }
    
    @Override
    public void run(final CommandSender sender, final String commandLabel, final Command cmd, final String[] args) {
        if (this.isPlayer(sender)) {
            final Player player = this.asPlayer(sender);
            if (this.permission != null && !player.hasPermission(this.permission)) {
                player.sendMessage(C.RED + "Invalid permissions!");
                return;
            }
        }
        else if (this.playerOnly) {
            sender.sendMessage(C.RED + "Only players can perform this command!");
            return;
        }
        this.run(sender, Arrays.copyOfRange(args, 1, args.length));
    }
    
    @Override
    public String getFullUsage() {
        return ChatColor.AQUA + this.usage + ChatColor.RESET + " - " + ChatColor.YELLOW + this.description;
    }
    
    @Override
    public void sendFullUsage(final CommandSender sender) {
        sender.sendMessage(this.getFullUsage());
    }
    
    public boolean isPlayer(final CommandSender sender) {
        return sender instanceof Player;
    }
    
    public Player asPlayer(final CommandSender sender) {
        return (Player)sender;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<String> getAliases() {
        return this.aliases;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getUsage() {
        return this.usage;
    }
    
    public boolean isPlayerOnly() {
        return this.playerOnly;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setAliases(final List<String> aliases) {
        this.aliases = aliases;
    }
    
    public void setPermission(final String permission) {
        this.permission = permission;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setUsage(final String usage) {
        this.usage = usage;
    }
    
    public void setPlayerOnly(final boolean playerOnly) {
        this.playerOnly = playerOnly;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EzQueueCommand)) {
            return false;
        }
        final EzQueueCommand other = (EzQueueCommand)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$aliases = this.getAliases();
        final Object other$aliases = other.getAliases();
        Label_0102: {
            if (this$aliases == null) {
                if (other$aliases == null) {
                    break Label_0102;
                }
            }
            else if (this$aliases.equals(other$aliases)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$permission = this.getPermission();
        final Object other$permission = other.getPermission();
        Label_0139: {
            if (this$permission == null) {
                if (other$permission == null) {
                    break Label_0139;
                }
            }
            else if (this$permission.equals(other$permission)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        Label_0176: {
            if (this$description == null) {
                if (other$description == null) {
                    break Label_0176;
                }
            }
            else if (this$description.equals(other$description)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$usage = this.getUsage();
        final Object other$usage = other.getUsage();
        if (this$usage == null) {
            if (other$usage == null) {
                return this.isPlayerOnly() == other.isPlayerOnly();
            }
        }
        else if (this$usage.equals(other$usage)) {
            return this.isPlayerOnly() == other.isPlayerOnly();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof EzQueueCommand;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $aliases = this.getAliases();
        result = result * 59 + (($aliases == null) ? 43 : $aliases.hashCode());
        final Object $permission = this.getPermission();
        result = result * 59 + (($permission == null) ? 43 : $permission.hashCode());
        final Object $description = this.getDescription();
        result = result * 59 + (($description == null) ? 43 : $description.hashCode());
        final Object $usage = this.getUsage();
        result = result * 59 + (($usage == null) ? 43 : $usage.hashCode());
        result = result * 59 + (this.isPlayerOnly() ? 79 : 97);
        return result;
    }
    
    @Override
    public String toString() {
        return "EzQueueCommand(name=" + this.getName() + ", aliases=" + this.getAliases() + ", permission=" + this.getPermission() + ", description=" + this.getDescription() + ", usage=" + this.getUsage() + ", playerOnly=" + this.isPlayerOnly() + ")";
    }
    
    public static List<EzQueueCommand> getCommands() {
        return EzQueueCommand.commands;
    }
    
    static {
        EzQueueCommand.commands = new ArrayList<EzQueueCommand>();
    }
}
