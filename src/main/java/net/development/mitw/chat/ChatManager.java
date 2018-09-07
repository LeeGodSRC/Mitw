package net.development.mitw.chat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import lombok.Getter;
import net.development.mitw.Mitw;
import net.development.mitw.events.ConfigurationReloadEvent;
import net.development.mitw.hooks.LuckPerms;

public class ChatManager implements Listener {

	@Getter
	private Set<UUID> playerMuted = new HashSet<>();
	private Mitw plugin;

	private Set<String> LV1_TOXIC = new HashSet<>();
	private Set<String> LV2_TOXIC = new HashSet<>();
	private Set<String> LV3_TOXIC = new HashSet<>();
	
	private List<String> TOXIC_REPLACEMENT = new ArrayList<>();

	public ChatManager(Mitw plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		load();
	}
	
	public void load() {
		LV1_TOXIC.clear();
		LV1_TOXIC.addAll(plugin.getConfigManager().getChatConfig().getStringList("lv1"));
		LV2_TOXIC.clear();
		LV2_TOXIC.addAll(plugin.getConfigManager().getChatConfig().getStringList("lv2"));
		LV3_TOXIC.clear();
		LV3_TOXIC.addAll(plugin.getConfigManager().getChatConfig().getStringList("lv3"));
		TOXIC_REPLACEMENT.clear();
		TOXIC_REPLACEMENT.addAll(plugin.getConfigManager().getChatConfig().getStringList("random-replace"));
	}

	public String getChatPrefix(Player player) {
		String luckpermsPrefix = LuckPerms.getPrefix(player);

		StringBuilder builder = new StringBuilder(luckpermsPrefix);

		for (ChatHandler chatHandler : plugin.getChatHandlers()) {
			builder.append(chatHandler.getPrefix(player));
		}

		return builder.toString();
	}

	public String getSuffixPrefix(Player player) {
		String luckpermsPrefix = LuckPerms.getSuffix(player);

		StringBuilder builder = new StringBuilder(luckpermsPrefix);

		for (ChatHandler chatHandler : plugin.getChatHandlers()) {
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

		for (String toxic : LV1_TOXIC) {
			if (message.toUpperCase().contains(toxic)) {
				return true;
			}
		}

		for (String messageSplitted : message.split(" ")) {
			for (String toxic : LV2_TOXIC) {
				if (messageSplitted.toUpperCase().contains(toxic.toUpperCase())
						&& Bukkit.getPlayer(messageSplitted) == null) {
					return true;
				}
			}
			for (String toxic : LV3_TOXIC) {
				if (messageSplitted.toUpperCase().equals(toxic)) {
					return true;
				}
			}
		}

		return false;

	}
	
	@EventHandler
	public void onConfigurationReload(ConfigurationReloadEvent e) {
		load();
	}

}
