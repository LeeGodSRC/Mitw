package mitw.bungee;

import java.util.*;

import mitw.bungee.commands.*;
import mitw.bungee.config.impl.MySQL;
import mitw.bungee.jedis.JedisSettings;
import mitw.bungee.jedis.MitwJedis;
import mitw.bungee.language.ILanguageData;
import mitw.bungee.language.LanguageAPI;
import mitw.bungee.language.types.SQLLanguageData;
import mitw.bungee.language.LanguageSQLConnection;
import mitw.bungee.modules.MotdDisplay;
import mitw.bungee.listener.BungeeListener;
import mitw.bungee.managers.CommandManager;
import mitw.bungee.config.impl.General;
import mitw.bungee.language.impl.Lang;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import taboolib.mysql.builder.hikari.HikariHandler;

@Getter
public class Mitw extends Plugin {

	public static Mitw INSTANCE;
	public static Map<UUID, UUID> replys = new HashMap<>();
	public static String Prefix;
	public static String CPrefix;
	private ILanguageData languageData;
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
		HikariHandler.init();
		final General General = new General(this);
		General.setup();

		mitwJedis = new MitwJedis(new JedisSettings(General.JEDIS_ADDRESS, General.JEDIS_PORT, General.JEDIS_PASSWORD));

		languageData = new SQLLanguageData(this, new LanguageSQLConnection(MySQL.LANGUAGE_DATABASE));
		language = new LanguageAPI(LanguageAPI.LangType.CLASS, this, languageData, new Lang());

		registerCommands();
		registerListeners();
		new CommandManager(this);

	}

	@Override
	public void onDisable() {
		final ArrayList<String> tempUUID = new ArrayList<>();
		for (final UUID u : General.Ignores) {
			tempUUID.add(u.toString());
		}
		General.getGeneral().set("ignore", tempUUID);
		General.getGeneral().save();
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
				new Memory(),
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
