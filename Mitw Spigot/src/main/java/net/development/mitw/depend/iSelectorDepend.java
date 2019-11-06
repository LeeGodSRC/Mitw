package net.development.mitw.depend;

import net.development.mitw.Mitw;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class iSelectorDepend {

    public static boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("iSelector");
    }

    public static void register(Mitw plugin) {
        if (!isEnabled()) {
            return;
        }

        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onProfileLoading(org.lgdev.iselector.api.events.ProfileLoadingEvent profileLoadingEvent) {
                profileLoadingEvent.setLanguage(plugin.getLanguageData().getLang(profileLoadingEvent.getPlayer()));
            }
        }, plugin);
    }

}
