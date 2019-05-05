package net.development.mitw.chat.slowchat;

import java.util.concurrent.TimeUnit;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.utils.ttl.TtlArrayList;

/**
 *
 * @author LeeGod
 * This system has been created because of the chat spammer
 * Hope its work fine
 */

public class ChatSlowerAI extends BukkitRunnable {

	private final Mitw plugin;

	@Getter
	@Setter
	private boolean aiEnable = false;

	private long lastMessage = -1;
	private double warningLevel = 0.0D;
	@Getter
	private int warnBroadcast = -1;
	private int spams = 0;
	private final TtlArrayList<String> chatCache = new TtlArrayList<>(TimeUnit.SECONDS, 15L);

	public ChatSlowerAI(final Mitw plugin) {
		this.plugin = plugin;
		if (!aiEnable) {
			return;
		}
		this.runTaskTimerAsynchronously(plugin, 20L, 20L);
	}

	public void detectChatSpammer(final Player player, final String chat) {
		if (!aiEnable) {
			return;
		}
		final int length = chat.length();
		if (length < 2) {
			if (Mitw.getRandom().nextFloat() > 0.8)
				return;
		}
		boolean clearSpams = true;
		for (final String cache : chatCache) {
			if (cache.indexOf(chat) > length / 1.5) {
				spams++;
				clearSpams = false;
				break;
			} else if (cache.startsWith(chat)) {
				spams++;
				clearSpams = false;
				break;
			} else if (cache.endsWith(chat)) {
				spams++;
				clearSpams = false;
				break;
			}
		}
		if (clearSpams) {
			spams = 0;
		}
		if (spams > 50) {
			this.warningLevel+=3.0D;
		} else if (spams > 30) {
			this.warningLevel+=1.8D;
		} else if (spams > 10) {
			this.warningLevel+=0.5D;
		} else {
			this.warningLevel-=0.1D;
		}
		final long lastDiff = System.currentTimeMillis() - this.lastMessage;
		if (lastDiff < 100L) {
			this.warningLevel+=3.0D;
		} else if (lastDiff < 300L) {
			this.warningLevel+=1.0D;
		} else if (lastDiff < 500L) {
			this.warningLevel+=0.5D;
		} else if (lastDiff < 700L) {
			this.warningLevel+=0.2D;
		}
		if (this.warningLevel > 10.0D && warnBroadcast != 1) {
			warnBroadcast = 1;
			Bukkit.broadcastMessage("§e[ChatSlowerAI] MitwAI: §fWarning! the chat seems like is getting spamming! Please stop doing it! Chat slower level is now on 1!");
		} else if (this.warningLevel > 20.0D && warnBroadcast != 2) {
			warnBroadcast = 2;
			Bukkit.broadcastMessage("§e[ChatSlowerAI] MitwAI: §fWarning! the chat seems like is getting spamming! Please stop doing it! Chat slower level is now on §c2§f!");
		} else if (this.warningLevel > 30.0D && warnBroadcast != 3) {
			warnBroadcast = 3;
			Bukkit.broadcastMessage("§e[ChatSlowerAI] MitwAI: §fWarning! the chat seems like is getting spamming! Please stop doing it! Chat slower level is now on §43§f!");
		} else if (this.warningLevel > 50.0D && warnBroadcast != 4) {
			warnBroadcast = 4;
			Bukkit.broadcastMessage("§e[ChatSlowerAI] MitwAI: §fWarning! the chat seems like is getting spamming! Please stop doing it! Chat is now disband for 10 minutes!");
		}
		lastMessage = System.currentTimeMillis();
	}

	@Override
	public void run() {
		if (warningLevel > 0.0D) {
			warningLevel = warningLevel - 0.05D > 0.0D ? warningLevel - 0.05D : 0.0D;
			if (warnBroadcast == 1 && warningLevel < 10.0D) {
				Bukkit.broadcastMessage("§e[ChatSlowerAI] MitwAI: §fChat slower level is now on §70§f ! Enjoy!");
				warningLevel = -1;
			} else if (warnBroadcast == 2 && warningLevel < 20.0D) {
				Bukkit.broadcastMessage("§e[ChatSlowerAI] MitwAI: §fChat slower level is now on 1 !");
				warningLevel = 1;
			} else if (warnBroadcast == 3 && warningLevel < 30.0D) {
				Bukkit.broadcastMessage("§e[ChatSlowerAI] MitwAI: §fChat slower level is now on §c2§f !");
				warningLevel = 2;
			} else if (warnBroadcast == 4 && warningLevel < 40.0D) {
				Bukkit.broadcastMessage("§e[ChatSlowerAI] MitwAI: §fEnabled chat! Chat slower level is now on §43§f !");
				warningLevel = 3;
			}
		}
	}

	public boolean isChatEnabled() {
		return warnBroadcast != 4;
	}

	public int getChatCooldown() {
		return warnBroadcast == -1 ? 3 : warnBroadcast == 1 ? 5 : warnBroadcast == 2 ? 10 : warnBroadcast == 3 ? 30 : Integer.MAX_VALUE;
	}

}
