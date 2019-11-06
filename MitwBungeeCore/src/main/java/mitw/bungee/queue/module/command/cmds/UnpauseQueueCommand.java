package mitw.bungee.queue.module.command.cmds;

import mitw.bungee.queue.module.QueueMessages;
import mitw.bungee.queue.module.queue.Queue;
import mitw.bungee.queue.module.util.C;
import mitw.bungee.queue.module.util.EzQueueUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;

public class UnpauseQueueCommand extends Command
{
    public UnpauseQueueCommand() {
        super("unpausequeue", "mitw.admin", new String[] { "upq" });
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage((BaseComponent) EzQueueUtil.toComponent(String.valueOf(C.RED) + "Usage: /unpausequeue <server>"));
            return;
        }
        final Queue queue = Queue.getQueue(args[0]);
        if (queue == null) {
            sender.sendMessage((BaseComponent) QueueMessages.getMsg().getInvalidQueueMessage());
            return;
        }
        queue.setPaused(false);
        sender.sendMessage((BaseComponent) QueueMessages.getMsg().getUnpauseQueueMessage(queue.getInfo().getName()));
    }
}
