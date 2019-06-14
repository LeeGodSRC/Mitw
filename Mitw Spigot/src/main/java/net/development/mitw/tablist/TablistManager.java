package net.development.mitw.tablist;

import lombok.Getter;
import net.development.mitw.tablist.abstraction.PacketUtil;
import net.development.mitw.tablist.abstraction.PacketUtil8;
import net.development.mitw.tablist.tab.Tab;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TablistManager implements Listener {

    @Getter
    private static TablistManager instance;

    private Map<Player, Tab> tabList = new HashMap<>();
    private PacketUtil packetUtil;

    public void enable(Plugin plugin) {
        instance = this;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        if (this.getServerVersion().equals("v1_7_R4")) {
            //TODO: 1.7
        } else {
            packetUtil = new PacketUtil8();
        }
    }

    public void disable(Plugin plugin) {
        for (final Tab tab : this.tabList.values()) {
            tab.clearTab();
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        if (this.hasTab(e.getPlayer())) {
            this.tabList.remove(e.getPlayer());
        }
    }

    public String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public void updateTabs() {
        for (final Tab tab : this.tabList.values()) {
            tab.updateTab();
        }
    }

    private Tab getTab(final Player player) {
        return this.tabList.get(player);
    }

    private boolean hasTab(final Player player) {
        return this.tabList.containsKey(player);
    }

    public Tab newTab(final Player player, final String tabTitle, final String tabFooter) {
        final Tab tab = new Tab(player, tabTitle, tabFooter);
        this.tabList.put(player, tab);
        return tab;
    }

    public void onDisable() {
        for (final Tab tab : this.tabList.values()) {
            tab.clearTab();
        }
    }

    public Map<Player, Tab> getTabs() {
        return this.tabList;
    }

}
