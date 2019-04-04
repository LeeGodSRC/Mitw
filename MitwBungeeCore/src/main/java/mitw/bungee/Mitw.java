package mitw.bungee;

import java.util.*;

import mitw.bungee.config.impl.MySQL;
import mitw.bungee.jedis.MitwJedis;
import mitw.bungee.language.LanguageAPI;
import mitw.bungee.language.types.SQLLanguageData;
import mitw.bungee.language.LanguageSQLConnection;
import mitw.bungee.modules.MotdDisplay;
import mitw.bungee.commands.AC;
import mitw.bungee.commands.Broadcast;
import mitw.bungee.commands.Ignore;
import mitw.bungee.commands.Lobby;
import mitw.bungee.commands.Message;
import mitw.bungee.commands.Reload;
import mitw.bungee.commands.Reply;
import mitw.bungee.commands.Report;
import mitw.bungee.commands.Server;
import mitw.bungee.listener.BungeeListener;
import mitw.bungee.managers.CommandManager;
import mitw.bungee.managers.YamlManagers;
import mitw.bungee.language.impl.Lang;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

@Getter
public class Mitw extends Plugin {

	public static Mitw INSTANCE;
	public static Map<UUID, UUID> replys = new HashMap<>();
	public static String Prefix;
	public static String CPrefix;
	private SQLLanguageData SQLLanguageData;
	private LanguageAPI language;
	private MitwJedis mitwJedis;
	public static List<String> servers = new ArrayList<>();

	static {
		Prefix = "§7[§6Mitw§7] §f";
		CPrefix = "§7[§6Mitw§f控制台§7] §f";
	}

	@Override
	public void onEnable() {
		INSTANCE = this;
		registerSgAlertServer();

		MySQL.init();

		SQLLanguageData = new SQLLanguageData(this, new LanguageSQLConnection(MySQL.LANGUAGE_DATABASE));
		language = new LanguageAPI(LanguageAPI.LangType.CLASS, this, SQLLanguageData, new Lang());

		registerCommands();
		registerListeners();
		new CommandManager(this);
		final YamlManagers YamlManagers = new YamlManagers(this);
		YamlManagers.setup();

	}

	@Override
	public void onDisable() {
		final ArrayList<String> tempUUID = new ArrayList<>();
		for (final UUID u : YamlManagers.Ignores) {
			tempUUID.add(u.toString());
		}
		YamlManagers.General.set("ignore", tempUUID);
		YamlManagers.saveConfig();
	}

	private void registerSgAlertServer() {
		servers.add("waiting");
		servers.add("duel");
		servers.add("ffa");
	}

	public void registerCommands() {
		Arrays.asList(
				new Lobby(this),
				new Server(this),
				new Message(),
				new Reply(),
				new Report(),
				new AC(),
				new Ignore(),
				new Broadcast(),
				new Reload(this)
		).forEach(command -> getProxy().getPluginManager().registerCommand(this, command));
	}

	public void registerCommand(final Command... cmds) {
	    for (Command command : cmds) {
	        getProxy().getPluginManager().registerCommand(this, command);
        }
    }

	private void registerListeners() {
		Arrays.asList(
				new MotdDisplay(this),
				new BungeeListener()
		).forEach(listener -> getProxy().getPluginManager().registerListener(this, listener));
	}
}
