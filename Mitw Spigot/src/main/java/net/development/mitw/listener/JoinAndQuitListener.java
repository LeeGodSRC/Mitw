package net.development.mitw.listener;

import net.development.mitw.Mitw;
import net.development.mitw.events.LanguageLoadedEvent;
import net.development.mitw.player.MitwPlayer;
import net.development.mitw.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.development.mitw.uuid.UUIDCache;

public class JoinAndQuitListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPreLogin(AsyncPlayerLoginEvent event) {

		Player bukkitPlayer = event.getPlayer();
		MitwPlayer player = new MitwPlayer(bukkitPlayer.getUniqueId(), bukkitPlayer.getName());
		player.load(() -> Bukkit.getPluginManager().callEvent(new LanguageLoadedEvent(bukkitPlayer, player.getLanguage())));

		if (Mitw.getInstance().getMitwJedis().isActive()) {
			UUIDCache.update(bukkitPlayer.getName(), bukkitPlayer.getUniqueId());
		}
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
	}

}
