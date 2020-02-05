package net.development.mitw.language;

import java.lang.reflect.Field;
import java.util.*;

import com.esotericsoftware.reflectasm.FieldAccess;
import net.development.mitw.player.MitwPlayer;
import net.development.mitw.utils.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

@Getter
@Setter
public class LanguageAPI extends AbstractLanguageAPI {

	public enum LangType {
		CONFIG, CLASS
	};


	private Map<String, String> savedMessages = new HashMap<>();
	private Map<String, List<String>> savedMessageLists = new HashMap<>();

	private Plugin plugin;
	private LangType type;
	private Object clazz;
	private FieldAccess fieldAccess;
	private final ILanguageData languageData;

	public LanguageAPI(final LangType type, final Plugin plugin, final ILanguageData languageData, final Object clazz) {
		this.type = type;
		this.clazz = clazz;
		this.fieldAccess = FieldAccess.get(clazz.getClass());
		this.languageData = languageData;
	}

	public String translate(MitwPlayer mitwPlayer, String format) {

		if (mitwPlayer == null) {
			return "";
		}

		format = StringUtil.replace(format, ".", "_");

		String language = mitwPlayer.getLanguage();

		if (language == null || language.isEmpty() || !ArrayUtils.contains(ILanguageData.LANGUAGES, language)) {
			language = ILanguageData.DEFAULT_LANGUAGE;
		}

		String format_2 = language + "_" + format;

		if (savedMessages.containsKey(format_2)) {
			return savedMessages.get(format_2);
		} else {
			String to;
			boolean found = false;
			try {
				Object object = fieldAccess.get(clazz, format_2);
				if (object == null) {
					object = fieldAccess.get(clazz, languageData.DEFAULT_LANGUAGE + "_" + format);
				} else {
					found = true;
				}
				to = (String) object;
			} catch (final Exception e) {
				Bukkit.getConsoleSender().sendMessage("§cCant get string field " + format_2.replace(".", "_") + " from " + clazz.getClass().getName()
						+ " from player " + mitwPlayer.getName() + " !");
				return "null";
			}
			if (found) {
				to = ChatColor.translateAlternateColorCodes('&', to);
				savedMessages.put(format_2, to);
			}
			return to;
		}
	}


	public List<String> translateArrays(final MitwPlayer mitwPlayer, String format) {

		if (mitwPlayer == null) {
			return Collections.singletonList("");
		}

		format = StringUtil.replace(format, ".", "_");
		String language = mitwPlayer.getLanguage();

		if (language == null || language.isEmpty() || !ArrayUtils.contains(ILanguageData.LANGUAGES, language)) {
			language = ILanguageData.DEFAULT_LANGUAGE;
		}

		String format_2 = language + "_" + format;

		if (savedMessageLists.containsKey(format_2))
			return savedMessageLists.get(format_2);
		else {
			List<String> to;
			boolean found = false;
			try {
				Object object = fieldAccess.get(clazz, format_2);
				if (object == null) {
					object = fieldAccess.get(clazz, languageData.DEFAULT_LANGUAGE + "_" + format);
				} else {
					found = true;
				}
				to = (List<String>) object;
			} catch (final Exception e) {
				Bukkit.getConsoleSender().sendMessage("§cCant get string field " + format_2.replace(".", "_") + " from " + clazz.getClass().getName()
						+ " from player " + mitwPlayer.getName() + " !");
				return Collections.singletonList("null");
			}
			if (found) {
				for (int i = 0; i < to.size(); i++) {
					to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
				}
				savedMessageLists.put(format_2, to);
			}
			return to;
		}
	}

}
