package net.development.mitw.listener;

import java.util.UUID;

import me.GoodestEnglish.QoolNick.QoolNickAPI;
import net.development.mitw.player.MitwPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.development.mitw.Mitw;
import net.development.mitw.chat.ChatManager;
import net.development.mitw.chat.PlayerCache;
import net.development.mitw.chat.check.Check;
import net.development.mitw.config.Settings;
import net.development.mitw.utils.Common;

public class ChatListener implements org.bukkit.event.Listener {

	private final Mitw plugin;

	public ChatListener(final Mitw plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(final AsyncPlayerChatEvent event) {

		final Player player = event.getPlayer();
		final MitwPlayer mitwPlayer = MitwPlayer.getByUuid(player.getUniqueId());
		final UUID uuid = player.getUniqueId();
		final PlayerCache cache = ChatManager.getPlayerCaches(uuid);
		String message = event.getMessage();

		if (mitwPlayer.getNextInput() != null) {
			mitwPlayer.getNextInput().accept(message);
			mitwPlayer.setNextInput(null);
			event.setCancelled(true);
			return;
		}

		final int cooldownTime = (int) ((System.currentTimeMillis() - cache.getLastTalkTime()) / 1000);

		if (!plugin.getChatManager().getChatSlowerAI().isChatEnabled()) {
			event.setCancelled(true);
			player.sendMessage("§cChat is disband by §eMitwAI");
			return;
		}

		final int cooldown = plugin.getChatManager().getChatSlowerAI().getChatCooldown();
		if (cooldownTime < cooldown) {
			event.setCancelled(true);
			Common.tell(player, Mitw.getInstance().getCoreLanguage().translate(player, "cooldownChat").replaceAll("<sec>",
					cooldown - cooldownTime + ""));
			return;
		}

		if (Settings.IS_NO_SAME_MESSAGE && message.equalsIgnoreCase(cache.getLastMessage())) {

			event.setCancelled(true);
			Common.tell(player, Mitw.getInstance().getCoreLanguage().translate(player, "noSpam"));
			return;

		}

		if (!(message.toLowerCase().equals("gg") || message.toLowerCase().equals("gf"))) {
			cache.setLastMessage(message);
		}

		cache.setLastTalkTime(System.currentTimeMillis());

		final Check c = Check.isSafeMessage(message);
		if (c != null) {
			if (Settings.IS_REPLACE_MODE) {
				message = plugin.getChatManager().getRandomMessages();
			} else {
				event.setCancelled(true);
				final ConsoleCommandSender sender = Bukkit.getConsoleSender();
				switch (c.getName().toLowerCase()) {
				case "high":
					Bukkit.dispatchCommand(sender, "bungee mute " + player.getName() + " 3h saying [" + message + "] -s");
					break;
				case "low":
					Bukkit.dispatchCommand(sender, "bungee mute " + player.getName() + " 1h saying [" + message + "] -s");
					break;
				case "single":
					Bukkit.dispatchCommand(sender, "bungee mute " + player.getName() + " 3h saying [" + message + "] -s");
					break;
				}
				return;
			}

		}

		message = message.replaceAll("<3", "\u2764").replaceAll("%", "%%");
		event.setMessage(message);

		plugin.getChatManager().getChatSlowerAI().detectChatSpammer(player, message);

		if (!Mitw.getInstance().getChatManager().isChatFormat()) {
			return;
		}

		final String prefix = plugin.getChatManager().getChatPrefix(player);
		final String suffix = plugin.getChatManager().getChatSuffix(player);

		if (Settings.IS_BETTER_NICK && QoolNickAPI.isNicked(player)) {
			event.setFormat(prefix + player.getName() + "§f: " + message);
			return;
		}

		if (((message.toLowerCase().equals("gg") || message.toLowerCase().equals("gf")) && event.getPlayer().hasPermission("mChat.goldgg"))) {
			event.setFormat(prefix + event.getPlayer().getName() + suffix + "§f: " + ChatColor.GOLD + message);
			return;
		}
		if (event.getPlayer().hasPermission("mChat.color")) {
			event.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + event.getPlayer().getName() + suffix + "&f: " + message));
		} else {
			event.setFormat(prefix + event.getPlayer().getName() + suffix + "§f: " + message);
		}

		plugin.getChatManager().getChatSlowerAI().detectChatSpammer(player, message);

	}

}
