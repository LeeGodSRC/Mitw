package net.development.mitw.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import net.development.mitw.config.Security;

public class PlayerLoginListener implements Listener {
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {

		if (!Security.ALLOW_IPS.contains(e.getRealAddress().getHostAddress())) {
			e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Security.KICK_MESSAGE);
		}
	}
}
