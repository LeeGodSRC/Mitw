package net.development.mitw.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import net.development.mitw.Mitw;

public class Configuration extends YamlConfiguration {

	@Getter
	private final String fileName;

	@Getter
	private final File file;

	public Configuration(final String name) {
		this.fileName = name;

		final Mitw plugin = Mitw.getInstance();

		file = new File(plugin.getDataFolder(), name + ".yml");

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}

		if (!file.exists()) {
			plugin.saveResource(fileName + ".yml", false);
		}

		try {
			super.load(file);
			System.out.println("file " + fileName + ".yml has been created.");
		} catch (final Exception e) {
			System.out.println("an error happend on creating file " + fileName + ".yml");
			e.printStackTrace();
		}

	}

	public void save() {
		try {
			super.save(file);
		} catch (final IOException e) {
			System.out.println("an error happend on saving file " + fileName + ".yml");
			e.printStackTrace();
		}
	}

	public void reload() {
		try {
			super.load(file);
			System.out.println("file " + fileName + ".yml has been reloaded.");
		} catch (final Exception e) {
			System.out.println("an error happend on reloading file " + fileName + ".yml");
			e.printStackTrace();
		}
	}

}
