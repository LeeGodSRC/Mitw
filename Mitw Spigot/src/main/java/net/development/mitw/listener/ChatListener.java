package net.development.mitw.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.development.mitw.Mitw;

public class ChatListener implements org.bukkit.event.Listener {

	private Mitw plugin;

	public ChatListener(Mitw plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();

		if(plugin.getChatManager().isPlayerMuted(player)) {
			player.sendMessage(plugin.getCoreLanguage().translate(player, "ismuted"));
			e.setCancelled(true);
			return;
		}
		
		String message = e.getMessage();

		String prefix = plugin.getChatManager().getChatPrefix(player);
		String suffix = plugin.getChatManager().getSuffixPrefix(player);

		if (plugin.getChatManager().containsToxicWords(message)) {
			plugin.getChatManager().mute(player);
			message = plugin.getChatManager().getRandomMessages();
		}
		
		e.setFormat(prefix + player.getName() + suffix + ": " + ChatColor.WHITE + message);
	}

}
