package mitw.bungee.queue.module.command.cmds;

import mitw.bungee.queue.module.QueueMessages;
import mitw.bungee.queue.module.command.PlayerOnlyCommand;
import mitw.bungee.queue.module.queue.Queue;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class QueueCommand extends PlayerOnlyCommand
{
    public QueueCommand() {
        super("queue");
    }
    
    @Override
    public void onExecute(final ProxiedPlayer player, final String[] args) {
        if (!Queue.inQueue(player)) {
            player.sendMessage((BaseComponent) QueueMessages.getMsg().getNotInQueueMessage());
            return;
        }
        final Queue queue = Queue.getQueue(player);
        player.sendMessage((BaseComponent) QueueMessages.getMsg().getQueueInfoMessage(queue.getInfo().getName(), queue.getPosition(player), queue.size()));
    }
}
