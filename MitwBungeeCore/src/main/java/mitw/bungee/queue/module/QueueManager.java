package mitw.bungee.queue.module;

import mitw.bungee.Mitw;
import mitw.bungee.queue.module.command.cmds.*;
import mitw.bungee.queue.module.tasks.impl.PositionMessageTask;
import mitw.bungee.queue.module.tasks.impl.QueueInfoTask;
import mitw.bungee.queue.module.tasks.impl.QueueSendTask;
import mitw.bungee.queue.module.util.ConfigFile;
import mitw.bungee.queue.shared.BungeeChannel;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

public class QueueManager
{
    private static QueueManager instance;
    private ConfigFile config;
    private QueueMessages msg;
    
    public void onLoad() {
        QueueManager.instance = this;
    }
    
    public void onEnable() {
        this.config = new ConfigFile(Mitw.INSTANCE);
        this.msg = new QueueMessages(this.config);
        Mitw.INSTANCE.getProxy().getPluginManager().registerListener(Mitw.INSTANCE, (Listener)new QueueListener(this));
        this.loadChannels();
        this.loadTasks();
        this.loadCommands();
    }
    
    private void loadChannels() {
        BungeeChannel[] values;
        for (int length = (values = BungeeChannel.values()).length, i = 0; i < length; ++i) {
            final BungeeChannel channel = values[i];
            Mitw.INSTANCE.getProxy().registerChannel(channel.getChannel());
        }
    }
    
    private void loadTasks() {
        new PositionMessageTask(this);
        new QueueInfoTask(this);
        new QueueSendTask(this);
    }
    
    private void loadCommands() {
        Mitw.INSTANCE.getProxy().getPluginManager().registerCommand(Mitw.INSTANCE, (Command)new LeaveQueueCommand());
        Mitw.INSTANCE.getProxy().getPluginManager().registerCommand(Mitw.INSTANCE, (Command)new PauseQueueCommand());
        Mitw.INSTANCE.getProxy().getPluginManager().registerCommand(Mitw.INSTANCE, (Command)new QueueCommand());
        Mitw.INSTANCE.getProxy().getPluginManager().registerCommand(Mitw.INSTANCE, (Command)new SetLimitCommand());
        Mitw.INSTANCE.getProxy().getPluginManager().registerCommand(Mitw.INSTANCE, (Command)new UnpauseQueueCommand());
    }
    
    public static QueueManager getInstance() {
        return QueueManager.instance;
    }
    
    public ConfigFile getConfig() {
        return this.config;
    }
    
    public QueueMessages getMsg() {
        return this.msg;
    }
}
