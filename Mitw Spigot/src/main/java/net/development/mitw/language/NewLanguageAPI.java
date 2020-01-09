package net.development.mitw.language;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import net.development.mitw.Mitw;
import net.development.mitw.player.MitwPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.utils.ClassUtil;
import net.development.mitw.utils.RV;
import net.development.mitw.utils.StringUtil;
import net.md_5.bungee.api.ChatColor;

public class NewLanguageAPI extends AbstractLanguageAPI {

	@Getter
	@Setter
	private Map<String, List<String>> savedMessages = new HashMap<>();

	@Getter
	@Setter
	private Plugin plugin;
	@Getter
	private final ILanguageData languageData;
	@Getter
	private final Map<String, FileConfiguration> languageFileMap = new HashMap<>();

	public NewLanguageAPI(final Plugin plugin, final ILanguageData languageData) {
		this.plugin = plugin;
		this.languageData = languageData;

		try {

			final String[] list = ClassUtil.getResourceListing(plugin.getClass(), "languages/");

			String next;
			for (int i = 0; i < list.length; i++) {
				next = list[i];
				plugin.getServer().getConsoleSender().sendMessage(next);
				if (!next.endsWith(".lang")) {
					continue;
				}
				final FileConfiguration data = YamlConfiguration.loadConfiguration(plugin.getResource("languages/" + next));
				this.languageFileMap.put(data.getString("lang"), data);
			}

		} catch (final Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	public NewLanguageAPI(final Plugin plugin, final ILanguageData languageData, final String languageFolder) {
		this.plugin = plugin;
		this.languageData = languageData;

		try {

			final String[] list = ClassUtil.getResourceListing(plugin.getClass(), languageFolder + "/");

			String next;
			for (int i = 0; i < list.length; i++) {
				next = list[i];
				plugin.getServer().getConsoleSender().sendMessage(next);
				if (!next.endsWith(".lang")) {
					continue;
				}
				final FileConfiguration data = YamlConfiguration.loadConfiguration(plugin.getResource(languageFolder + "/" + next));
				this.languageFileMap.put(data.getString("lang"), data);
			}

		} catch (final Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	public String translate(final MitwPlayer p, final String from) {
		final String lang = p.getLanguage();
		if (savedMessages.containsKey(lang + "." + from))
			return savedMessages.get(lang + "." + from).get(0);
		else {

			final FileConfiguration config = this.languageFileMap.get(lang);

			if (config == null) {
				Bukkit.getConsoleSender().sendMessage("cannot find the language: " + lang);
				return "null";
			}

			String to = config.getString(from);
			final boolean found = false;
			if (to == null) {
				Bukkit.getConsoleSender().sendMessage("cannot find " + from + " in config " + lang);
				return "null";
			} else {
				to = ChatColor.translateAlternateColorCodes('&', to);
				savedMessages.put(lang + "." + from, Arrays.asList(to));
			}

			return to;
		}
	}

	public List<String> translateArrays(final MitwPlayer p, final String from) {
		final String lang = p.getLanguage();
		if (savedMessages.containsKey(lang + "." + from))
			return savedMessages.get(lang + "." + from);
		else {
			final FileConfiguration config = this.languageFileMap.get(lang);

			if (config == null) {
				Bukkit.getConsoleSender().sendMessage("cannot find the language: " + lang);
				return Arrays.asList("null");
			}

			final List<String> to = config.getStringList(from);
			boolean found = false;
			if (to == null)
				return Arrays.asList("null");
			else {
				found = true;
			}

			if (found) {
				for (int i = 0; i < to.size(); i++) {
					to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
				}
				savedMessages.put(lang + "." + from, to);
			}
			return to;
		}
	}
}
