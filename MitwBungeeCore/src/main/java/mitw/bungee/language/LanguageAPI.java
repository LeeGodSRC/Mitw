package mitw.bungee.language;

import com.esotericsoftware.reflectasm.FieldAccess;
import mitw.bungee.config.YamlConfiguration;
import mitw.bungee.util.RV;
import mitw.bungee.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class LanguageAPI {

	public static enum LangType {
		CONFIG, CLASS
	};

	@Getter
	@Setter
	private Map<String, List<String>> savedMessagesList = new HashMap<>();
	private Map<String, String> savedMessages = new HashMap<>();

	@Getter
	@Setter
	private Plugin plugin;
	@Getter
	@Setter
	private LangType type;
	@Getter
	@Setter
	private YamlConfiguration config;
	private FieldAccess fieldAccess;
	@Getter
	@Setter
	private Object clazz;
	@Getter
	private final ILanguageData languageData;

	public LanguageAPI(final LangType type, final Plugin plugin, final ILanguageData languageData, final Object clazz) {
		this.type = type;
		this.clazz = clazz;
		this.languageData = languageData;
		this.fieldAccess = FieldAccess.get(clazz.getClass());
	}

	public void send(final ProxiedPlayer p, final String translateMessage) {
		p.sendMessage(translate(p, translateMessage));
	}

	public void send(final ProxiedPlayer p, final String translateMessage, final RV... replaceValue) {
		p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue));
	}

	public void send(final String translateMessage) {
		ProxyServer.getInstance().getPlayers().forEach(p -> p.sendMessage(translate(p, translateMessage)));
	}

	public void send(final String translateMessage, final RV... replaceValue) {
		ProxyServer.getInstance().getPlayers().forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
	}

	public void send(final Set<ProxiedPlayer> player, final String translateMessage) {
		player.forEach(p -> p.sendMessage(translate(p, translateMessage)));
	}

	public void send(final Set<ProxiedPlayer> player, final String translateMessage, final RV... replaceValue) {
		player.forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
	}

	public void send(final List<ProxiedPlayer> player, final String translateMessage) {
		player.forEach(p -> p.sendMessage(translate(p, translateMessage)));
	}

	public void send(final List<ProxiedPlayer> player, final String translateMessage, final RV... replaceValue) {
		player.forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
	}

	public void send2(final Set<UUID> player, final String translateMessage) {
		send(player.stream().map(ProxyServer.getInstance()::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage);
	}

	public void send2(final Set<UUID> player, final String translateMessage, final RV... replaceValue) {
		send(player.stream().map(ProxyServer.getInstance()::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage, replaceValue);
	}

	public void send2(final List<UUID> player, final String translateMessage) {
		send(player.stream().map(ProxyServer.getInstance()::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage);
	}

	public void send2(final List<UUID> player, final String translateMessage, final RV... replaceValue) {
		send(player.stream().map(ProxyServer.getInstance()::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage, replaceValue);
	}

	public String translate(final ProxiedPlayer p, final String ofrom, final RV... replaceValues) {
		return StringUtil.replace(translate(p, ofrom), replaceValues);
	}

	public String translate(final ProxiedPlayer p, final String ofrom) {
		final String lang = languageData.getLang(p);
		final String from = lang + "_" + ofrom;
		if (savedMessages.containsKey(from)) {
			return savedMessages.get(from);
		} else {
			String to = null;
			boolean found = false;
			Object object = fieldAccess.get(clazz, from);
			if (object == null) {
				object = fieldAccess.get(clazz, languageData.DEFAULT_LANGUAGE + "_" + ofrom);
			} else {
				found = true;
			}
			to = (String) object;
			if (found) {
				to = ChatColor.translateAlternateColorCodes('&', to);
				savedMessages.put(from, to);
			}
			return to;
		}
	}

	public List<String> translateArrays(final ProxiedPlayer p, final String ofrom, final RV... replaceValues) {
		return translateArrays(p, ofrom).stream().map(string -> StringUtil.replace(string, replaceValues)).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public List<String> translateArrays(final ProxiedPlayer p, final String ofrom) {
		final String lang = languageData.getLang(p);
		final String from = lang + "_" + ofrom;
		if (savedMessagesList.containsKey(from))
			return savedMessagesList.get(from);
		else {
			List<String> to = null;
			boolean found = false;
			Object object = fieldAccess.get(clazz, from);
			if (object == null) {
				object = fieldAccess.get(clazz, languageData.DEFAULT_LANGUAGE + "_" + ofrom);
			} else {
				found = true;
			}
			to = (List<String>) object;
			if (found) {
				for (int i = 0; i < to.size(); i++) {
					to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
				}
				savedMessagesList.put(from, to);
			}
			return to;
		}
	}

}
