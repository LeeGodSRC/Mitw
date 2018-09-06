package net.development.mitw.config;

import org.bukkit.Bukkit;

import lombok.Getter;
import net.development.mitw.events.ConfigurationReloadEvent;
import net.development.mitw.utils.Configuration;

public class ConfigManager {

	@Getter
	private Configuration mainConfig;
	@Getter
	private Configuration chatConfig;
	
	public ConfigManager() {
		this.mainConfig = new Configuration("config");
		this.chatConfig = new Configuration("chat");
	}
	
	public void reloadAll() {
		chatConfig.reload();
		Bukkit.getPluginManager().callEvent(new ConfigurationReloadEvent());
		System.out.println("[Mitw - ConfigManager] all configuration has been reloaded");
	}

}
