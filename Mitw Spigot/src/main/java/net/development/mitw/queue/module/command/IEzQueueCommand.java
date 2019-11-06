package net.development.mitw.queue.module.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface IEzQueueCommand
{
    String getFullUsage();
    
    void run(final CommandSender p0, final String p1, final Command p2, final String[] p3);
    
    void run(final CommandSender p0, final String[] p1);
    
    void sendFullUsage(final CommandSender p0);
}
