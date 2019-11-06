package mitw.bungee.queue.module.tasks;

import mitw.bungee.Mitw;
import mitw.bungee.queue.module.QueueManager;
import mitw.bungee.queue.module.queue.Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class EzQueueTask implements Runnable
{
    private QueueManager plugin;
    
    @Override
    public abstract void run();
    
    public void enable(final long interval) {
        Mitw.INSTANCE.getProxy().getScheduler().schedule(Mitw.INSTANCE, (Runnable)this, 0L, interval, TimeUnit.MILLISECONDS);
    }
    
    public List<Queue> getQueues() {
        return new ArrayList<Queue>(Queue.getQueues());
    }
    
    public QueueManager getPlugin() {
        return this.plugin;
    }
    
    public EzQueueTask(final QueueManager plugin) {
        this.plugin = plugin;
    }
}
