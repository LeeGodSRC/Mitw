package net.development.mitw;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class Mitw extends JavaPlugin {
	
	@Getter private static Mitw instance;
	
	@Override
	public void onEnable() {
		instance = this;
	}

}
