package mitw.bungee.queue.module.command.cmds;

import mitw.bungee.queue.module.QueueManager;
import mitw.bungee.queue.module.QueueMessages;
import mitw.bungee.queue.module.queue.Queue;
import mitw.bungee.queue.module.util.C;
import mitw.bungee.queue.module.util.EzQueueUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Command;

public class SetLimitCommand extends Command
{
    public SetLimitCommand() {
        super("setlimit", "mitw.admin", new String[] { "sl" });
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length < 2) {
            sender.sendMessage((BaseComponent) EzQueueUtil.toComponent(String.valueOf(C.RED) + "Usage: /setlimit <server> <amount>"));
            return;
        }
        final Queue queue = Queue.getQueue(args[0]);
        if (queue == null) {
            sender.sendMessage((BaseComponent) QueueMessages.getMsg().getInvalidQueueMessage());
            return;
        }
        int limit = 0;
        try {
            limit = Integer.parseInt(args[1]);
        }
        catch (Exception e) {
            sender.sendMessage((BaseComponent) EzQueueUtil.toComponent(String.valueOf(C.RED) + "Usage: /setlimit <server> <amount>"));
            return;
        }
        queue.setLimit((limit == -1) ? limit : Math.abs(limit));
        QueueManager.getInstance().getConfig().setObject("servers." + queue.getName() + ".limit", limit);
        sender.sendMessage((BaseComponent) EzQueueUtil.toComponent(String.valueOf(C.GREEN) + "Set " + queue.getName() + " limit to " + limit + "."));
    }
}
