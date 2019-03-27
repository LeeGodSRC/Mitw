package net.development.mitw.listener;

import net.development.mitw.Mitw;
import net.development.mitw.utils.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.development.mitw.uuid.UUIDCache;

public class JoinAndQuitListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		if (Mitw.getInstance().getMitwJedis().isActive()) {
			UUIDCache.update(event.getName(), event.getUniqueId());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		PlayerUtil.allowMovement(player);
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e) {

	}

}
