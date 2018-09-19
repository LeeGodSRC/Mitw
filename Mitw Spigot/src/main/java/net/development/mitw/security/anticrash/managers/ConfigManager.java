package net.development.mitw.security.anticrash.managers;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;
import com.google.common.io.*;
import java.io.*;
import org.bukkit.configuration.*;

public class ConfigManager {
	private File configfile;
	private YamlConfiguration config;

	public ConfigManager(final JavaPlugin plugin, final String configname) {
		this.configfile = null;
		this.config = null;
		if (!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		this.configfile = new File(plugin.getDataFolder(), configname);
		if (!this.configfile.exists()) {
			try {
				this.configfile.createNewFile();
				final InputStream is = plugin.getResource(configname);
				final OutputStream os = new FileOutputStream(this.configfile);
				ByteStreams.copy(is, os);
			} catch (IOException e) {
				throw new RuntimeException("Unable to create configuration file", e);
			}
		}
		this.config = YamlConfiguration.loadConfiguration(this.configfile);
	}

	public Configuration getConfig() {
		return (Configuration) this.config;
	}

	public void saveConfig() {
		try {
			this.config.save(this.configfile);
		} catch (IOException e) {
			throw new RuntimeException("Unable to save configuration file", e);
		}
	}
}
