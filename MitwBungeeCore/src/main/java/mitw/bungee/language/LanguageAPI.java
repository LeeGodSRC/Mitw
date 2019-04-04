package mitw.bungee.language;

import mitw.bungee.config.YamlConfiguration;
import mitw.bungee.language.types.SQLLanguageData;
import mitw.bungee.util.RV;
import mitw.bungee.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class LanguageAPI {

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
	private final mitw.bungee.language.types.SQLLanguageData SQLLanguageData;

	public LanguageAPI(final LangType type, final Plugin plugin, final SQLLanguageData SQLLanguageData, final YamlConfiguration config) {
		this.type = type;
		this.config = config;
		this.plugin = plugin;
		this.SQLLanguageData = SQLLanguageData;
	}

	public LanguageAPI(final LangType type, final Plugin plugin, final SQLLanguageData SQLLanguageData, final Object clazz) {
		this.type = type;
		this.clazz = clazz;
		this.SQLLanguageData = SQLLanguageData;
	}

	public LanguageAPI(final LangType type, final Plugin plugin, final SQLLanguageData SQLLanguageData, final Class<?> clazz, final Class<?>... methods) {
		this.type = type;
		this.plugin = plugin;
		this.SQLLanguageData = SQLLanguageData;
		try {
			this.clazz = clazz.getConstructor(methods).newInstance();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public LanguageAPI(final LangType type, final Plugin plugin, final SQLLanguageData SQLLanguageData, final YamlConfiguration config,
					   final Class<?> clazz, final Class<?>... methods) {
		this.type = type;
		this.config = config;
		this.plugin = plugin;
		this.SQLLanguageData = SQLLanguageData;
		try {
			this.clazz = clazz.getConstructor(methods).newInstance();
		} catch (final Exception e) {
			e.printStackTrace();
		}
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
		final String lang = SQLLanguageData.getLang(p);
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
						field = clazz.getClass().getDeclaredField((SQLLanguageData.DEFAULT_LANGUAGE + "_" + ofrom).replace(".", "_"));
						field.setAccessible(true);
						object = field.get(clazz);
					} else {
						found = true;
					}
					to = (String) object;
				} catch (final Exception e) {
					ProxyServer.getInstance().getConsole().sendMessage("§cCant get string field " + from.replace(".", "_") + " from " + clazz.getClass().getName()
							+ " from player " + p.getName() + " !");
					return "null";
				}
				break;
			case CONFIG:
				String notsure = config.getString(from);
				if (notsure == null) {
					notsure = config.getString(SQLLanguageData.DEFAULT_LANGUAGE + "." + ofrom);
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

	public List<String> translateArrays(final ProxiedPlayer p, final String ofrom, final RV... replaceValues) {
		return translateArrays(p, ofrom).stream().map(string -> StringUtil.replace(string, replaceValues)).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public List<String> translateArrays(final ProxiedPlayer p, final String ofrom) {
		final String lang = SQLLanguageData.getLang(p);
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
						field = clazz.getClass().getDeclaredField((SQLLanguageData.DEFAULT_LANGUAGE + "_" + ofrom).replace(".", "_"));
						field.setAccessible(true);
						object = field.get(clazz);
					} else {
						found = true;
					}
					to = (List<String>) object;
				} catch (final Exception e) {
					ProxyServer.getInstance().getConsole().sendMessage("§cCant get string field " + from.replace(".", "_") + " from " + clazz.getClass().getName()
							+ " from player " + p.getName() + " !");
					return Arrays.asList("null");
				}
				break;
			case CONFIG:
				List<String> notsure = config.getStringList(from);
				if (notsure == null) {
					notsure = config.getStringList(SQLLanguageData.DEFAULT_LANGUAGE + "." + ofrom);
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
