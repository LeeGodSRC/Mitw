package net.development.mitw.listener;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.domedd.betternick.api.nickedplayer.NickedPlayer;
import net.development.mitw.Mitw;
import net.development.mitw.chat.ChatManager;
import net.development.mitw.chat.PlayerCache;
import net.development.mitw.chat.check.Check;
import net.development.mitw.config.Settings;
import net.development.mitw.utils.Common;

public class ChatListener implements org.bukkit.event.Listener {

	private final Mitw plugin;

	public ChatListener(Mitw plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		final Player p = e.getPlayer();
		final UUID u = p.getUniqueId();
		final PlayerCache cache = ChatManager.getPlayerCaches(u);
		String message = e.getMessage();
		final double cooldownTime = (System.currentTimeMillis() - cache.getLastTalkTime()) / 1000d;
		if (cache.isMute()) {
			if (cooldownTime < Settings.MUTE_TIME) {
				e.setCancelled(true);
				Common.tell(p, "&c抱歉 你目前處於禁言狀態,你還需要約&f " + (Settings.MUTE_TIME - (int) cooldownTime) + " &c秒後才能說話");
				return;
			}
			cache.setMute(false);
		}
		if (cooldownTime < Settings.CHAT_COOLDOWN) { // 還沒冷卻結束
			e.setCancelled(true);
			Common.tell(p, "&6請放慢你的說話速度,你還要&e " + (Settings.CHAT_COOLDOWN - (int) cooldownTime) + " 秒後才能說話");
			return;
		}
		if (Settings.IS_NO_SAME_MESSAGE && message.equalsIgnoreCase(cache.getLastMessage())) {
			e.setCancelled(true);
			Common.tell(p, "&c請不要嘗試發送的同樣訊息");
			return;
		}
		if (!(message.toLowerCase().equals("gg") || message.toLowerCase().equals("gf")))
			cache.setLastMessage(message);
		cache.setLastTalkTime(System.currentTimeMillis());

		if (!Check.isSafeMessage(message)) {
			e.setCancelled(true);
			Common.tell(p, "&c請不要說難聽的話,謝謝你");
			cache.setMute(true);
			return;
		}

		message = message.replaceAll("LOL", "哈哈").replaceAll("<3", "\u2764");

		if (Settings.IS_BETTER_NICK) {
			final NickedPlayer nickP = new NickedPlayer(p);
			if (nickP.isNicked()) {
				e.setFormat("§7[§b1000§7]" + nickP.getName() + "§f: " + message);
				return;
			}
		}

		final String prefix = plugin.getChatManager().getChatPrefix(p);
		final String suffix = plugin.getChatManager().getSuffixPrefix(p);
		e.setMessage(message);
		if (((message.toLowerCase().equals("gg") || message.toLowerCase().equals("gf")) && e.getPlayer().hasPermission("mChat.goldgg"))) {
			e.setFormat(prefix + e.getPlayer().getName() + suffix + "§f: " + ChatColor.GOLD + message);
			return;
		}
		if (e.getPlayer().hasPermission("mChat.color"))
			e.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + e.getPlayer().getName() + suffix + "&f: " + message));
		else
			e.setFormat(prefix + e.getPlayer().getName() + suffix + "§f: " + message);

	}

	ExecutorService executor = Executors.newSingleThreadExecutor();

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		executor.execute(() -> ChatManager.getPlayerCaches(e.getPlayer().getUniqueId()).save());
	}

}
