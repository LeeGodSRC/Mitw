package net.development.mitw.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import net.development.mitw.config.Security;

public class PlayerLoginListener implements Listener {
	@EventHandler
	public void onLogin(AsyncPlayerPreLoginEvent e) {

		if (!Security.ALLOW_IPS.contains(e.getAddress().getHostAddress())) {
			e.disallow(Result.KICK_WHITELIST, Security.KICK_MESSAGE);
		}
	}
}
