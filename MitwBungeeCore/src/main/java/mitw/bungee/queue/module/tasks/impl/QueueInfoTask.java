package mitw.bungee.queue.module.tasks.impl;

import mitw.bungee.queue.module.QueueManager;
import mitw.bungee.queue.module.tasks.EzQueueTask;
import mitw.bungee.queue.module.util.EzQueueUtil;

public class QueueInfoTask extends EzQueueTask
{
    public QueueInfoTask(final QueueManager plugin) {
        super(plugin);
        this.enable(250L);
    }
    
    @Override
    public void run() {
        EzQueueUtil.sendQueueInfo();
    }
}
