package net.development.mitw.language;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import net.development.mitw.language.types.SQLLanguageData;
import net.development.mitw.player.MitwPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.utils.RV;
import net.development.mitw.utils.StringUtil;

public abstract class SimpleLanguageAPI extends AbstractLanguageAPI {

	private static ILanguageData data = Mitw.getInstance().getLanguageData();

	@Getter
	@Setter
	private Map<String, List<String>> savedMessages = new HashMap<>();

	@Getter
	@Setter
	private Plugin plugin;

	public SimpleLanguageAPI(final Plugin plugin) {
		this.plugin = plugin;
		getMessages().entrySet().forEach(entry -> savedMessages.put(entry.getKey(), entry.getValue().stream().map(s -> StringUtil.cc(s)).collect(Collectors.toList())));
	}

	public abstract Map<String, List<String>> getMessages();

	public String translate(final MitwPlayer p, final String ofrom) {
		final String lang = p.getLanguage();
		final String from = lang + "_" + ofrom;
		if (savedMessages.containsKey(from))
			return savedMessages.get(from).get(0);
		else {
			Bukkit.getConsoleSender().sendMessage("can't find the string from " + from);
			return "null";
		}
	}

	public List<String> translateArrays(final MitwPlayer p, final String ofrom) {
		final String lang = p.getLanguage();
		final String from = lang + "_" + ofrom;
		if (savedMessages.containsKey(from))
			return savedMessages.get(from);
		else {
			Bukkit.getConsoleSender().sendMessage("can't find the string from " + from);
			return Arrays.asList("null");
		}
	}

	public static class SimpleHashMap extends HashMap<String, List<String>> {
		private static final long serialVersionUID = 1L;

		public SimpleHashMap p(final String from, final String... to) {
			super.put(from, Arrays.asList(to));
			return this;
		}
	}

}
