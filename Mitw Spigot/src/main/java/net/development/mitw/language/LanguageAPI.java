package net.development.mitw.language;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.development.mitw.player.MitwPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public class LanguageAPI extends AbstractLanguageAPI {

	public static enum LangType {
		CONFIG, CLASS
	};

	@Getter
	@Setter
	private Map<String, List<String>> savedMessages = new HashMap<>();

	@Getter
	@Setter
	private Plugin plugin;
	@Getter
	@Setter
	private LangType type;
	@Getter
	@Setter
	private YamlConfiguration config;
	@Getter
	@Setter
	private Object clazz;
	@Getter
	private final ILanguageData languageData;

	public LanguageAPI(final LangType type, final Plugin plugin, final ILanguageData languageData, final YamlConfiguration config) {
		this.type = type;
		this.config = config;
		this.plugin = plugin;
		this.languageData = languageData;
	}

	public LanguageAPI(final LangType type, final Plugin plugin, final ILanguageData languageData, final Object clazz) {
		this.type = type;
		this.clazz = clazz;
		this.languageData = languageData;
	}

	public LanguageAPI(final LangType type, final Plugin plugin, final ILanguageData languageData, final Class<?> clazz, final Class<?>... methods) {
		this.type = type;
		this.plugin = plugin;
		this.languageData = languageData;
		try {
			this.clazz = clazz.getConstructor(methods).newInstance();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public LanguageAPI(final LangType type, final Plugin plugin, final ILanguageData languageData, final YamlConfiguration config,
			final Class<?> clazz, final Class<?>... methods) {
		this.type = type;
		this.config = config;
		this.plugin = plugin;
		this.languageData = languageData;
		try {
			this.clazz = clazz.getConstructor(methods).newInstance();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public String translate(MitwPlayer p, final String ofrom) {
		final String lang = p.getLanguage();
		final String from = lang + "." + ofrom;
		if (savedMessages.containsKey(from)) {
			return savedMessages.get(from).get(0);
		} else {
			String to = null;
			boolean found = false;
			switch (type) {
			case CLASS:
				try {
					Field field = clazz.getClass().getDeclaredField(from.replace(".", "_"));
					Object object = field.get(clazz);
					if (object == null) {
						field = clazz.getClass().getDeclaredField((languageData.DEFAULT_LANGUAGE + "_" + ofrom).replace(".", "_"));
						field.setAccessible(true);
						object = field.get(clazz);
					} else {
						found = true;
					}
					to = (String) object;
				} catch (final Exception e) {
					Bukkit.getConsoleSender().sendMessage("§cCant get string field " + from.replace(".", "_") + " from " + clazz.getClass().getName()
							+ " from player " + p.getName() + " !");
					return "null";
				}
				break;
			case CONFIG:
				String notsure = config.getString(from);
				if (notsure == null) {
					notsure = config.getString(languageData.DEFAULT_LANGUAGE + "." + ofrom);
				} else {
					found = true;
				}
				to = notsure;
				break;
			}
			if (found) {
				to = ChatColor.translateAlternateColorCodes('&', to);
				savedMessages.put(from, Arrays.asList(to));
			}
			return to;
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> translateArrays(final MitwPlayer p, final String ofrom) {
		final String lang = p.getLanguage();
		final String from = lang + "." + ofrom;
		if (savedMessages.containsKey(from))
			return savedMessages.get(from);
		else {
			List<String> to = null;
			boolean found = false;
			switch (type) {
			case CLASS:
				try {
					Field field = clazz.getClass().getDeclaredField(from.replace(".", "_"));
					Object object = field.get(clazz);
					if (object == null) {
						field = clazz.getClass().getDeclaredField((languageData.DEFAULT_LANGUAGE + "_" + ofrom).replace(".", "_"));
						field.setAccessible(true);
						object = field.get(clazz);
					} else {
						found = true;
					}
					to = (List<String>) object;
				} catch (final Exception e) {
					Bukkit.getConsoleSender().sendMessage("§cCant get string field " + from.replace(".", "_") + " from " + clazz.getClass().getName()
							+ " from player " + p.getName() + " !");
					return Arrays.asList("null");
				}
				break;
			case CONFIG:
				List<String> notsure = config.getStringList(from);
				if (notsure == null) {
					notsure = config.getStringList(languageData.DEFAULT_LANGUAGE + "." + ofrom);
				} else {
					found = true;
				}
				to = notsure;
				break;
			}
			if (found) {
				for (int i = 0; i < to.size(); i++) {
					to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
				}
				savedMessages.put(from, to);
			}
			return to;
		}
	}

}
