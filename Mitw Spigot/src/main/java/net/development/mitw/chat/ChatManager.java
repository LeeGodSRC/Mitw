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

import de.domedd.betternick.api.nickedplayer.NickedPlayer;
import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.chat.check.HighCheck;
import net.development.mitw.chat.check.LowCheck;
import net.development.mitw.chat.check.SingleCheck;
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

	private final List<String> TOXIC_REPLACEMENT = new ArrayList<>();

	public ChatManager(Mitw plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		load();
	}

	public void load() {
		Settings.init();
		new HighCheck(Settings.CHECK_HIGH);
		new LowCheck(Settings.CHECK_LOW);
		new SingleCheck(Settings.CHECK_SINGLE);
	}

	public String getChatPrefix(Player player) {
		final String luckpermsPrefix;
		if (Settings.IS_BETTER_NICK) {
			final NickedPlayer nickP = new NickedPlayer(player);
			if (nickP.isNicked())
				luckpermsPrefix = "&7";
			else
				luckpermsPrefix = LuckPerms.getPrefix(player);
		} else {
			luckpermsPrefix = LuckPerms.getPrefix(player);
		}

		final StringBuilder builder = new StringBuilder();

		for (final ChatHandler chatHandler : plugin.getChatHandlers()) {
			final String prefix = chatHandler.getPrefix(player);
			if (prefix == null || prefix.isEmpty())
				continue;
			builder.append(chatHandler.getPrefix(player));
		}
		if (luckpermsPrefix != null)
			builder.append(luckpermsPrefix);

		return Common.colored(builder.toString());
	}

	public String getSuffixPrefix(Player player) {
		final String luckpermsPrefix;
		if (Settings.IS_BETTER_NICK) {
			final NickedPlayer nickP = new NickedPlayer(player);
			if (nickP.isNicked())
				luckpermsPrefix = "&7";
			else
				luckpermsPrefix = LuckPerms.getSuffix(player);
		} else {
			luckpermsPrefix = LuckPerms.getSuffix(player);
		}
		final StringBuilder builder = new StringBuilder();

		for (final ChatHandler chatHandler : plugin.getChatHandlers()) {
			final String suffix = chatHandler.getSuffix(player);
			if (suffix == null || suffix.isEmpty())
				continue;
			builder.append(chatHandler.getSuffix(player));
		}
		builder.append(luckpermsPrefix);
		return Common.colored(builder.toString());
	}

	public String getRandomMessages() {
		return TOXIC_REPLACEMENT.get(Mitw.getRandom().nextInt(TOXIC_REPLACEMENT.size()));
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
