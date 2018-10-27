package net.development.mitw.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.development.mitw.uuid.UUIDCache;

public class JoinAndQuitListener implements Listener {

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		UUIDCache.update(player.getName(), player.getUniqueId());
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent e) {

	}

}
