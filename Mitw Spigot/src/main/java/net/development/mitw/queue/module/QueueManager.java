package net.development.mitw.queue.module;

import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.config.Configuration;
import net.development.mitw.queue.module.command.EzQueueMainCommand;
import net.development.mitw.queue.module.command.cmds.PlayCommand;
import net.development.mitw.queue.module.util.ConfigUtil;
import net.development.mitw.queue.shared.BungeeChannel;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class QueueManager
{
    private static QueueManager instance;
    private ConfigUtil configUtil;
    @Getter
    private Configuration config;
    
    public void onLoad() {
        QueueManager.instance = this;
    }
    
    public void onEnable() {
        this.config = new Configuration("queue", true);
        (this.configUtil = new ConfigUtil(this)).load();
        this.loadListeners();
        this.loadCommands();
    }
    
    private void loadListeners() {
        final QueueListener listener = new QueueListener();
        Bukkit.getPluginManager().registerEvents((Listener)listener, Mitw.getInstance());
        for (final BungeeChannel channel : BungeeChannel.values()) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(Mitw.getInstance(), channel.getChannel());
            Bukkit.getMessenger().registerIncomingPluginChannel(Mitw.getInstance(), channel.getChannel(), (PluginMessageListener)listener);
        }
    }
    
    private void loadCommands() {
        Mitw.getInstance().getCommand("queueSystem").setExecutor((CommandExecutor)new EzQueueMainCommand());
        Mitw.getInstance().getCommand("play").setExecutor((CommandExecutor)new PlayCommand());
    }
    
    public ConfigUtil getConfigUtil() {
        return this.configUtil;
    }
    
    public static QueueManager getInstance() {
        return QueueManager.instance;
    }
}
