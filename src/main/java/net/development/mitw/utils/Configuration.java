package net.development.mitw.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

import lombok.Getter;
import net.development.mitw.Mitw;

public class Configuration extends YamlConfiguration {
	
	@Getter
	private String fileName;
	
	@Getter
	private File file;
	
	public Configuration(String name) {
		file = new File(name + ".yml");
		
		Mitw plugin = Mitw.getInstance();
		
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdirs();
		}
		
		if (!file.exists()) {
			Mitw.getInstance().saveResource(fileName + ".yml", false);
		}
		
		try {
			super.load(file);
			System.out.println("file " + fileName + ".yml has been created.");
		} catch (Exception e) {
			System.out.println("an error happend on creating file " + fileName + ".yml");
			e.printStackTrace();
		}
		
	}
	
	public void save() {
		try {
			super.save(file);
		} catch (IOException e) {
			System.out.println("an error happend on saving file " + fileName + ".yml");
			e.printStackTrace();
		}
	}
	
	public void reload() {
		try {
			super.load(file);
			System.out.println("file " + fileName + ".yml has been reloaded.");
		} catch (Exception e) {
			System.out.println("an error happend on reloading file " + fileName + ".yml");
			e.printStackTrace();
		}
	}

}
