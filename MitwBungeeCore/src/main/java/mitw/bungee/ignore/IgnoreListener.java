package mitw.bungee.ignore;

import mitw.bungee.Mitw;
import mitw.bungee.config.Configuration;
import mitw.bungee.config.impl.Config;
import mitw.bungee.database.PlayerFlatFileData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class IgnoreListener implements Listener {

    private Mitw plugin;

    public IgnoreListener(Mitw plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            PlayerFlatFileData flatFileData = new PlayerFlatFileData(player.getUniqueId());
            Configuration config = flatFileData.load();
            plugin.getIgnoreManager().load(player.getUniqueId(), config);
        });
    }

    @EventHandler
    public void onQuit(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            PlayerFlatFileData flatFileData = new PlayerFlatFileData(player.getUniqueId());
            Configuration config = flatFileData.load();
            plugin.getIgnoreManager().saveAndClear(player.getUniqueId(), config);
            flatFileData.save();
        });
    }

}
