package mitw.bungee.queue.module.tasks.impl;

import mitw.bungee.queue.module.QueueManager;
import mitw.bungee.queue.module.tasks.EzQueueTask;
import mitw.bungee.queue.module.util.EzQueueUtil;

public class PositionMessageTask extends EzQueueTask
{
    public PositionMessageTask(final QueueManager plugin) {
        super(plugin);
        this.enable((long)plugin.getMsg().getSendMessageInterval() * 1000L);
    }
    
    @Override
    public void run() {
        EzQueueUtil.sendPositionMessage();
    }
}
