package net.development.mitw.listener;

import net.development.mitw.Mitw;
import net.development.mitw.events.LanguageLoadedEvent;
import net.development.mitw.events.player.PlayerFirstJoinEvent;
import net.development.mitw.player.MitwPlayer;
import net.development.mitw.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import net.development.mitw.uuid.UUIDCache;

public class JoinAndQuitListener implements Listener {

	private Mitw plugin;

	public JoinAndQuitListener(Mitw plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		if (Mitw.getInstance().getMitwJedis().isActive()) {
			UUIDCache.update(event.getName(), event.getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			MitwPlayer mitwPlayer = new MitwPlayer(player.getUniqueId(), player.getName());
			boolean first = mitwPlayer.load();

			plugin.getServer().getScheduler().runTask(plugin, () -> {
				if (first) {
					plugin.getServer().getPluginManager().callEvent(new PlayerFirstJoinEvent(player));
				}
				plugin.getServer().getPluginManager().callEvent(new LanguageLoadedEvent(player, mitwPlayer.getLanguage()));
			});
		});
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		PlayerUtil.allowMovement(player);
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e) {
		Player player = e.getPlayer();

		MitwPlayer mitwPlayer = MitwPlayer.getByUuid(player.getUniqueId());
		mitwPlayer.save();
		mitwPlayer.unhandle();
	}

}
