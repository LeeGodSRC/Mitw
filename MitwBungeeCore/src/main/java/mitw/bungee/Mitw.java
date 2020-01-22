package mitw.bungee;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.ilummc.tlib.util.Strings;
import mitw.bungee.commands.*;
import mitw.bungee.config.Configuration;
import mitw.bungee.config.impl.Mongo;
import mitw.bungee.config.impl.MySQL;
import mitw.bungee.database.PlayerFlatFileData;
import mitw.bungee.ignore.IgnoreListener;
import mitw.bungee.ignore.IgnoreManager;
import mitw.bungee.jedis.JedisSettings;
import mitw.bungee.jedis.MitwJedis;
import mitw.bungee.jedis.server.KeepAliveHandler;
import mitw.bungee.language.LanguageAPI;
import mitw.bungee.language.types.RedisLanguageData;
import mitw.bungee.modules.MotdDisplay;
import mitw.bungee.listener.BungeeListener;
import mitw.bungee.managers.CommandManager;
import mitw.bungee.config.impl.General;
import mitw.bungee.language.impl.Lang;
import lombok.Getter;
import mitw.bungee.player.database.PlayerMongo;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import taboolib.mysql.builder.hikari.HikariHandler;

@Getter
public class Mitw extends Plugin {

	/**
	 *
	 * Mitw Bungeecord 計畫
	 * - Ignore sql saving plus ignore specfic players
	 *
	 */

	public static Mitw INSTANCE;
	public static Map<UUID, UUID> replys = new HashMap<>();
	public static String Prefix = "§7[§6Mitw§7] §f";
	public static String CPrefix = "§7[§6Mitw§f控制台§7] §f";
	private RedisLanguageData languageData;
	private LanguageAPI language;
	private PlayerMongo mongo;
	private MitwJedis mitwJedis;
	private KeepAliveHandler keepAliveHandler;
	private IgnoreManager ignoreManager;
//	private QueueManager queueManager;
	public static List<String> servers = Arrays.asList("waiting", "lobby-2", "lobby-3", "duel", "ffa");

	@Override
	public void onLoad() {
//		this.queueManager = new QueueManager();
//		this.queueManager.onLoad();
	}

	@Override
	public void onEnable() {
		INSTANCE = this;

		MySQL.init();
		HikariHandler.init();
		Mongo.init();
		final General General = new General(this);
		General.setup();

		ignoreManager = new IgnoreManager();

		mongo = new PlayerMongo();
		mitwJedis = new MitwJedis(new JedisSettings(General.JEDIS_ADDRESS, General.JEDIS_PORT, General.JEDIS_PASSWORD));

		keepAliveHandler = new KeepAliveHandler();

		this.getProxy().getScheduler().schedule(this, keepAliveHandler, 1000L, 30 * 1000L, TimeUnit.MILLISECONDS);

		languageData = new RedisLanguageData(this);
		language = new LanguageAPI(LanguageAPI.LangType.CLASS, this, languageData, new Lang());

//		this.queueManager.onEnable();

		registerCommands();
		registerListeners();
		new CommandManager(this);

	}

	@Override
	public void onDisable() {
		for (ProxiedPlayer player : getProxy().getPlayers()) {
			PlayerFlatFileData flatFileData = new PlayerFlatFileData(player.getUniqueId());
			Configuration config = flatFileData.load();
			this.getIgnoreManager().saveAndClear(player.getUniqueId(), config);
			flatFileData.save();
		}
	}

	public void registerCommands() {
		Arrays.asList(
//				new Lobby(this),
				new Server(this),
				new Message(),
				new Reply(),
				new Alert(),
				new Report(),
				new AC(),
				new Ignore(),
				new Broadcast(),
				new Memory(),
				new Reload(this)
		).forEach(command -> getProxy().getPluginManager().registerCommand(this, command));
	}

	public void registerCommand(Command... commands) {
	    for (Command command : commands) {
	        getProxy().getPluginManager().registerCommand(this, command);
        }
    }

	private void registerListeners() {
		Arrays.asList(
				new MotdDisplay(this),
				new BungeeListener(),
				new IgnoreListener(this)
		).forEach(listener -> getProxy().getPluginManager().registerListener(this, listener));
	}

	public void alertOnlySpecificServers(String connect, String serverName, String replaceOrder) {
		List<ProxiedPlayer> players = servers.stream().map(getProxy()::getServerInfo).map(ServerInfo::getPlayers)
				.flatMap(Collection::stream).collect(Collectors.toList());
		final int size = players.size();
		final int diff = (int) Math.ceil(players.size() / 20D);

		Map<String, BaseComponent[]> components = new HashMap<>();

		for (int i = 0, j = 0; i < size; i += diff) {

			if (i >= size)
				return;

			final int start = i;
			final int end = i + diff;
			getProxy().getScheduler().schedule(this, () -> {
				for (int i1 = start; i1 < end; ++i1) {
					if (i1 >= players.size())
						return;

					ProxiedPlayer player = players.get(i1);
					if (player.isConnected()) {
						String language = getLanguageData().getLang(player);
						BaseComponent[] text;
						if (components.containsKey(language)) {
							text = components.get(language);
						} else {
							text = new ComponentBuilder(Strings
									.replaceWithOrder(getLanguage().translate(player, "alert"), getLanguage().translate(player, serverName), replaceOrder))
									.append(getJson(connect, player)).create();
							components.put(language, text);
						}
						player.sendMessage(text);
					}
				}
			}, ++j * 50, TimeUnit.MILLISECONDS);

		}
	}

	public static TextComponent getJson(final String cmd, final ProxiedPlayer player) {
		final TextComponent msg = new TextComponent(Mitw.INSTANCE.getLanguage().translate(player, "clickjoin"));
		msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
		return msg;
	}
}
