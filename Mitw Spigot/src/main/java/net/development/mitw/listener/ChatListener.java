package net.development.mitw.listener;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.domedd.betternick.api.BetterNickAPI;
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
	public void onChat(final AsyncPlayerChatEvent e) {
		final Player p = e.getPlayer();
		final UUID u = p.getUniqueId();
		final PlayerCache cache = ChatManager.getPlayerCaches(u);
		String message = e.getMessage();
		final int cooldownTime = (int) ((System.currentTimeMillis() - cache.getLastTalkTime()) / 1000);
		if (!plugin.getChatManager().getChatSlowerAI().isChatEnabled()) {
			e.setCancelled(true);
			p.sendMessage("§cChat is disband by §eMitwAI");
		}
		final int cooldown = plugin.getChatManager().getChatSlowerAI().getChatCooldown();
		if (cooldownTime < cooldown) { // 還沒冷卻結束
			e.setCancelled(true);
			Common.tell(p, Mitw.getInstance().getCoreLanguage().translate(p, "cooldownChat").replaceAll("<sec>",
					cooldown - cooldownTime + ""));
			return;
		}
		if (Settings.IS_NO_SAME_MESSAGE && message.equalsIgnoreCase(cache.getLastMessage())) {
			e.setCancelled(true);
			Common.tell(p, Mitw.getInstance().getCoreLanguage().translate(p, "noSpam"));
			return;
		}
		if (!(message.toLowerCase().equals("gg") || message.toLowerCase().equals("gf"))) {
			cache.setLastMessage(message);
		}

		cache.setLastTalkTime(System.currentTimeMillis());

		final Check c = Check.isSafeMessage(message);
		if (c != null) {
			e.setCancelled(true);
			final CommandSender sender = Bukkit.getConsoleSender();
			switch (c.getName().toLowerCase()) {
			case "high":
				Bukkit.dispatchCommand(sender, "bungee mute " + p.getName() + " 3h saying [" + message + "] -s");
				break;
			case "low":
				Bukkit.dispatchCommand(sender, "bungee mute " + p.getName() + " 1h saying [" + message + "] -s");
				break;
			case "single":
				Bukkit.dispatchCommand(sender, "bungee mute " + p.getName() + " 3h saying [" + message + "] -s");
				break;
			}
			return;
		}
		message = message.replaceAll("<3", "\u2764").replaceAll("%", "%%");
		final String prefix = plugin.getChatManager().getChatPrefix(p);
		final String suffix = plugin.getChatManager().getSuffixPrefix(p);

		if (Settings.IS_BETTER_NICK && BetterNickAPI.getApi().isNicked(p)) {
			e.setFormat(prefix + p.getName() + "§f: " + message);
			return;
		}

		e.setMessage(message);
		if (((message.toLowerCase().equals("gg") || message.toLowerCase().equals("gf")) && e.getPlayer().hasPermission("mChat.goldgg"))) {
			e.setFormat(prefix + e.getPlayer().getName() + suffix + "§f: " + ChatColor.GOLD + message);
			return;
		}
		if (e.getPlayer().hasPermission("mChat.color")) {
			e.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + e.getPlayer().getName() + suffix + "&f: " + message));
		} else {
			e.setFormat(prefix + e.getPlayer().getName() + suffix + "§f: " + message);
		}

		plugin.getChatManager().getChatSlowerAI().detectChatSpammer(p, message);

	}

	ExecutorService executor = Executors.newSingleThreadExecutor();

	@EventHandler
	public void onQuit(final PlayerQuitEvent e) {
		executor.execute(() -> ChatManager.getPlayerCaches(e.getPlayer().getUniqueId()).save());
	}

}
