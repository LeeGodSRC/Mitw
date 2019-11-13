package net.development.mitw.chat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import lombok.Setter;
import me.GoodestEnglish.QoolNick.QoolNickAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.chat.check.CheckType;
import net.development.mitw.chat.check.HighCheck;
import net.development.mitw.chat.check.LowCheck;
import net.development.mitw.chat.check.SingleCheck;
import net.development.mitw.chat.slowchat.ChatSlowerAI;
import net.development.mitw.config.Settings;
import net.development.mitw.events.ConfigurationReloadEvent;
import net.development.mitw.hooks.LuckPerms;
import net.development.mitw.utils.Common;

public class ChatManager implements Listener {

	private static Cache<UUID, PlayerCache> playerCaches = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(20, TimeUnit.MINUTES)
			.build();

	@Getter
	private final Set<UUID> playerMuted = new HashSet<>();
	private final Mitw plugin;
	@Getter
	private final ChatDatabase chatDB;
	@Getter
	private final ChatSlowerAI chatSlowerAI;
	@Getter
	@Setter
	private boolean chatFormat = true;

	public ChatManager(final Mitw plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		chatDB = new ChatDatabase(plugin);
		chatSlowerAI = new ChatSlowerAI(plugin);
		load();
	}

	public void load() {
		Settings.init();
		new HighCheck(getChatDB().getAllWordsByType(CheckType.HIGH));
		new LowCheck(getChatDB().getAllWordsByType(CheckType.LOW));
		new SingleCheck(getChatDB().getAllWordsByType(CheckType.SINGLE));
	}

	public String getChatPrefix(final Player player) {
		final String luckpermsPrefix;
		if (Settings.IS_BETTER_NICK && QoolNickAPI.isNicked(player)) {
			luckpermsPrefix = QoolNickAPI.getNickedPrefix(player).equals("") ? "&7" : QoolNickAPI.getNickedPrefix(player);
		} else {
			luckpermsPrefix = LuckPerms.getPrefix(player);
		}

		final StringBuilder builder = new StringBuilder();

		for (final ChatHandler chatHandler : plugin.getChatHandlers()) {
			final String prefix = chatHandler.getPrefix(player);
			if (prefix == null || prefix.isEmpty()) {
				continue;
			}
			builder.append(chatHandler.getPrefix(player));
		}
		if (luckpermsPrefix != null) {
			builder.append(luckpermsPrefix);
		}

		return Common.colored(builder.toString());
	}

	public String getChatSuffix(final Player player) {
		final StringBuilder builder = new StringBuilder();

		for (final ChatHandler chatHandler : plugin.getChatHandlers()) {
			final String suffix = chatHandler.getSuffix(player);
			if (suffix == null || suffix.isEmpty()) {
				continue;
			}
			builder.append(chatHandler.getSuffix(player));
		}
		return Common.colored(builder.toString());
	}

	public String getRandomMessages() {
		return Settings.REPLACE_LIST.get(Mitw.getRandom().nextInt(Settings.REPLACE_LIST.size()));
	}

	public static PlayerCache getPlayerCaches(final UUID id) {
		PlayerCache cache = playerCaches.getIfPresent(id);
		if (cache == null) {
			cache = new PlayerCache(id);
			playerCaches.put(id, cache);
		}
		return cache;
	}

	@EventHandler
	public void onConfigurationReload(final ConfigurationReloadEvent e) {
		load();
	}

}
