package net.development.mitw.chat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.chat.check.Check;
import net.development.mitw.config.Settings;
import net.development.mitw.events.ConfigurationReloadEvent;
import net.development.mitw.hooks.LuckPerms;

public class ChatManager implements Listener {

	private static Cache<UUID, PlayerCache> playerCaches = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(20, TimeUnit.SECONDS).build();

	@Getter
	private final Set<UUID> playerMuted = new HashSet<>();
	private final Mitw plugin;

	private final List<String> TOXIC_REPLACEMENT = new ArrayList<>();

	public ChatManager(Mitw plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		load();
	}

	public void load() {
		Settings.init();
	}

	public String getChatPrefix(Player player) {
		final String luckpermsPrefix = LuckPerms.getPrefix(player);

		final StringBuilder builder = new StringBuilder(luckpermsPrefix);

		for (final ChatHandler chatHandler : plugin.getChatHandlers()) {
			builder.append(chatHandler.getPrefix(player));
		}

		return builder.toString();
	}

	public String getSuffixPrefix(Player player) {
		final String luckpermsPrefix = LuckPerms.getSuffix(player);

		final StringBuilder builder = new StringBuilder(luckpermsPrefix);

		for (final ChatHandler chatHandler : plugin.getChatHandlers()) {
			builder.append(chatHandler.getSuffix(player));
		}

		return builder.toString();
	}

	public boolean isPlayerMuted(Player player) {
		return isPlayerMuted(player.getUniqueId());
	}

	public boolean isPlayerMuted(UUID uuid) {
		return playerMuted.contains(uuid);
	}

	public void mute(Player player) {
		playerMuted.add(player.getUniqueId());
		player.sendMessage(plugin.getCoreLanguage().translate(player, "muted"));
	}

	public String getRandomMessages() {
		return TOXIC_REPLACEMENT.get(Mitw.getRandom().nextInt(TOXIC_REPLACEMENT.size()));
	}

	public boolean containsToxicWords(String message) {
		if (Check.isSafeMessage(message))
			return true;
		return false;

	}

	public static PlayerCache getPlayerCaches(UUID id) {
		PlayerCache cache = playerCaches.getIfPresent(id);
		if (cache == null) {
			cache = new PlayerCache(id);
			playerCaches.put(id, cache);
		}
		return cache;
	}

	@EventHandler
	public void onConfigurationReload(ConfigurationReloadEvent e) {
		load();
	}

}
