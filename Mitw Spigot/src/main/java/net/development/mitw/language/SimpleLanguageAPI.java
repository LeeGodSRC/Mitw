package net.development.mitw.language;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.utils.RV;
import net.development.mitw.utils.StringUtil;
import net.md_5.bungee.api.ChatColor;

public abstract class SimpleLanguageAPI {

	private static LanguageData data = Mitw.getInstance().getLanguageData();

	@Getter
	@Setter
	private Map<String, List<String>> savedMessages = new HashMap<>();

	@Getter
	@Setter
	private Plugin plugin;

	public SimpleLanguageAPI(final Plugin plugin) {
		this.plugin = plugin;
		savedMessages = getMessages();
		savedMessages.entrySet().forEach(entry -> savedMessages.put(entry.getKey(), entry.getValue().stream().map(s -> StringUtil.cc(s)).collect(Collectors.toList())));
	}

	public abstract Map<String, List<String>> getMessages();

	public void send(final Player p, final String translateMessage) {
		p.sendMessage(translate(p, translateMessage));
	}

	public void send(final Player p, final String translateMessage, final RV... replaceValue) {
		p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue));
	}

	public void send(final String translateMessage) {
		Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(translate(p, translateMessage)));
	}

	public void send(final String translateMessage, final RV... replaceValue) {
		Bukkit.getOnlinePlayers()
		.forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
	}

	public void send(final Set<Player> player, final String translateMessage) {
		player.forEach(p -> p.sendMessage(translate(p, translateMessage)));
	}

	public void send(final Set<Player> player, final String translateMessage, final RV... replaceValue) {
		player.forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
	}

	public void send(final List<Player> player, final String translateMessage) {
		player.forEach(p -> p.sendMessage(translate(p, translateMessage)));
	}

	public void send(final List<Player> player, final String translateMessage, final RV... replaceValue) {
		player.forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
	}

	public void send2(final Set<UUID> player, final String translateMessage) {
		send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()),
				translateMessage);
	}

	public void send2(final Set<UUID> player, final String translateMessage, final RV... replaceValue) {
		send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()),
				translateMessage, replaceValue);
	}

	public void send2(final List<UUID> player, final String translateMessage) {
		send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()),
				translateMessage);
	}

	public void send2(final List<UUID> player, final String translateMessage, final RV... replaceValue) {
		send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()),
				translateMessage, replaceValue);
	}

	public String translate(final Player p, final String ofrom, final RV... replaceValues) {
		return StringUtil.replace(translate(p, ofrom), replaceValues);
	}

	public String translate(final Player p, final String ofrom) {
		final String lang = data.getLang(p);
		final String from = lang + "." + ofrom;
		if (savedMessages.containsKey(from))
			return savedMessages.get(from).get(0);
		else {
			String to = null;
			boolean found = false;
			try {
				Field field = this.getClass().getDeclaredField(from.replace(".", "_"));
				Object object = field.get(this.getClass());
				if (object == null) {
					field = this.getClass().getClass()
							.getDeclaredField((LanguageData.DEFAULT_LANGUAGE + "_" + ofrom).replace(".", "_"));
					field.setAccessible(true);
					object = field.get(this.getClass());
				} else {
					found = true;
				}
				to = (String) object;
			} catch (final Exception e) {
				Bukkit.getConsoleSender().sendMessage("§cCant get string field " + from.replace(".", "_") + " from "
						+ this.getClass().getClass().getName() + " from player " + p.getName() + " !");
				return "null";
			}
			if (found) {
				savedMessages.put(from, Arrays.asList(to));
			} else {
				to = ChatColor.translateAlternateColorCodes('&', to);
			}
			return to;
		}
	}

	public List<String> translateArrays(final Player p, final String ofrom, final RV... replaceValues) {
		return translateArrays(p, ofrom).stream().map(string -> StringUtil.replace(string, replaceValues))
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public List<String> translateArrays(final Player p, final String ofrom) {
		final String lang = data.getLang(p);
		final String from = lang + "." + ofrom;
		if (savedMessages.containsKey(from))
			return savedMessages.get(from);
		else {
			List<String> to = null;
			boolean found = false;
			try {
				Field field = this.getClass().getDeclaredField(from.replace(".", "_"));
				Object object = field.get(this.getClass());
				if (object == null) {
					field = this.getClass().getClass()
							.getDeclaredField((LanguageData.DEFAULT_LANGUAGE + "_" + ofrom).replace(".", "_"));
					field.setAccessible(true);
					object = field.get(this.getClass());
				} else {
					found = true;
				}
				to = (List<String>) object;
			} catch (final Exception e) {
				Bukkit.getConsoleSender().sendMessage("§cCant get string field " + from.replace(".", "_") + " from "
						+ this.getClass().getClass().getName() + " from player " + p.getName() + " !");
				return Arrays.asList("null");
			}
			if (found) {
				savedMessages.put(from, to);
			} else {
				for (int i = 0; i < to.size(); i++) {
					to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
				}
			}
			return to;
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
