package net.development.mitw.language;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public class LanguageAPI {
	
	public static enum LangType {
		CONFIG, CLASS
	};
	
	@Getter @Setter private Map<String, List<String>> savedMessages = new HashMap<>();
	
	@Getter @Setter private Plugin plugin;
	@Getter @Setter private LangType type;
	@Getter @Setter private YamlConfiguration config;
	@Getter @Setter private Object clazz;
	@Getter private LanguageData languageData;
	
	public LanguageAPI(LangType type, Plugin plugin, LanguageData languageData, YamlConfiguration config) {
		this.type = type;
		this.config = config;
		this.plugin = plugin;
		this.languageData = languageData;
	}

	public LanguageAPI(LangType type, Plugin plugin, LanguageData languageData, Object clazz) {
		this.type = type;
		this.clazz = clazz;
		this.languageData = languageData;
	}

	public LanguageAPI(LangType type, Plugin plugin, LanguageData languageData, Class<?> clazz, Class<?>... methods) {
		this.type = type;
		this.plugin = plugin;
		this.languageData = languageData;
		try {
			this.clazz = clazz.getConstructor(methods).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LanguageAPI(LangType type, Plugin plugin, LanguageData languageData, YamlConfiguration config, Class<?> clazz, Class<?>... methods) {
		this.type = type;
		this.config = config;
		this.plugin = plugin;
		this.languageData = languageData;
		try {
			this.clazz = clazz.getConstructor(methods).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String translate(Player p, String ofrom) {
		String lang = languageData.getLang(p);
		String from = lang+"."+ofrom;
		if(savedMessages.containsKey(from)) {
			return savedMessages.get(from).get(0);
		} else {
			String to = null;
			boolean found = false;
			switch(type) {
			case CLASS:
				try {
					Field field = clazz.getClass().getDeclaredField(from.replace(".", "_"));
					Object object = field.get(clazz);
					if(object == null) {
						field = clazz.getClass().getDeclaredField((LanguageData.DEFAULT_LANGUAGE+"_"+ofrom).replace(".", "_"));
						field.setAccessible(true);
						object = field.get(clazz);
					} else {
						found = true;
					}
					to = (String)object;
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage("§cCant get string field "+from.replace(".", "_")+" from "+clazz.getClass().getName()+" from player "+p.getName()+" !");
					return "null";
				}
				break;
			case CONFIG:
				String notsure = config.getString(from);
				if(notsure == null) {
					notsure = config.getString(LanguageData.DEFAULT_LANGUAGE+"."+ofrom);
				} else {
					found = true;
				}
				to = notsure;
				break;
			}
			to = ChatColor.translateAlternateColorCodes('&', to);
			if(found) {
				savedMessages.put(from, Arrays.asList(to));
			}
			return to;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> translateArrays(Player p, String ofrom) {
		String lang = languageData.getLang(p);
		String from = lang+"."+ofrom;
		if(savedMessages.containsKey(from)) {
			return savedMessages.get(from);
		} else {
			List<String> to = null;
			boolean found = false;
			switch(type) {
			case CLASS:
				try {
					Field field = clazz.getClass().getDeclaredField(from.replace(".", "_"));
					Object object = field.get(clazz);
					if(object == null) {
						field = clazz.getClass().getDeclaredField((LanguageData.DEFAULT_LANGUAGE+"_"+ofrom).replace(".", "_"));
						field.setAccessible(true);
						object = field.get(clazz);
					} else {
						found = true;
					}
					to = (List<String>)object;
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage("§cCant get string field "+from.replace(".", "_")+" from "+clazz.getClass().getName()+" from player "+p.getName()+" !");
					return Arrays.asList("null");
				}
				break;
			case CONFIG:
				List<String> notsure = config.getStringList(from);
				if(notsure == null) {
					notsure = config.getStringList(LanguageData.DEFAULT_LANGUAGE+"."+ofrom);
				} else {
					found = true;
				}
				to = notsure;
				break;
			}
			for(int i = 0; i < to.size() ; i++) {
				to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
			}
			if(found) {
				savedMessages.put(from, to);
			}
			return to;
		}
	}

}
