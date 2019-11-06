package mitw.bungee.queue.module.command.cmds;

import mitw.bungee.queue.module.QueueMessages;
import mitw.bungee.queue.module.command.PlayerOnlyCommand;
import mitw.bungee.queue.module.queue.Queue;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LeaveQueueCommand extends PlayerOnlyCommand
{
    public LeaveQueueCommand() {
        super("leavequeue");
    }
    
    @Override
    public void onExecute(final ProxiedPlayer player, final String[] args) {
        if (!Queue.inQueue(player)) {
            player.sendMessage((BaseComponent) QueueMessages.getMsg().getNotInQueueMessage());
            return;
        }
        Queue.getQueue(player).remove(player);
    }
}
