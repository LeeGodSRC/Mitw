package mitw.bungee.queue.module.tasks.impl;

import mitw.bungee.queue.module.QueueManager;
import mitw.bungee.queue.module.tasks.EzQueueTask;

public class QueueSendTask extends EzQueueTask
{
    public QueueSendTask(final QueueManager plugin) {
        super(plugin);
        this.enable(100L);
    }
    
    @Override
    public void run() {
        this.getQueues().stream().filter(q -> q.canSend()).forEach(q -> q.sendNext());
    }
}
