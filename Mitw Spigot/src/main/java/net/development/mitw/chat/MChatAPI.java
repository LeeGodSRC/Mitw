package net.development.mitw.chat;

import org.bukkit.entity.Player;

public class MChatAPI {
	public static void putPlayerSuffix(Player p, String str) {
		final PlayerCache cache = ChatManager.getPlayerCaches(p.getUniqueId());
		cache.setSuffix(str);
		cache.save();

	}

	public static void removePlayerSuffix(Player p) {
		final PlayerCache cache = ChatManager.getPlayerCaches(p.getUniqueId());
		cache.setSuffix("");
		cache.save();
	}
}