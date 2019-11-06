package net.development.mitw.config;

import java.util.List;

import org.bukkit.Bukkit;

public class Settings extends Configuration {

	private Settings(final String fileName) {
		super(fileName);
	}

	public void loadVars() {

		addDefault("jedis.address", "127.0.0.1");
		JEDIS_ADDRESS = getString("jedis.address", "127.0.0.1");
		addDefault("jedis.port", 6379);
		JEDIS_PORT = getInt("jedis.port", 6379);
		addDefault("jedis.password", "");
		JEDIS_PASSWORD = getString("jedis.password", "");

		addDefault("keepAlive.enabled", false);
		KEEP_ALIVE_PACKET = getBoolean("keepAlive.enabled", false);
		addDefault("keepAlive.serverName", "lobby-1");
		KEEP_ALIVE_SERVER_NAME = getString("keepAlive.serverName", "lobby-1");

		IS_REPLACE_MODE = getBoolean("Replacemode.Enable");
		IS_NO_SAME_MESSAGE = getBoolean("AntiSpam.NoSameMessage");

		IS_BETTER_NICK = Bukkit.getPluginManager().getPlugin("BetterNick") != null;

		CHAT_COOLDOWN = getInt("AntiSpam.Cooldown");
		MUTE_TIME = getInt("MuteTime");

		CHECK_HIGH = getStringList("Checks.High");
		CHECK_LOW = getStringList("Checks.Low");
		CHECK_SINGLE = getStringList("Checks.Single");
		CHECK_EXCEPTION = getStringList("Checks.Exception");

		REPLACE_LIST = getStringList("Replacemode.ReplaceStringList");

		if (JEDIS_ADDRESS == null) {
			JEDIS_ADDRESS = "127.0.0.1";
			JEDIS_PORT = 6379;
			JEDIS_PASSWORD = "52156";
		}

		save();
	}

	public static void init() {
		final Settings s = new Settings("settings");
		s.loadVars();
	}

	public static boolean IS_REPLACE_MODE,IS_NO_SAME_MESSAGE;
	public static boolean IS_BETTER_NICK, KEEP_ALIVE_PACKET;
	public static int CHAT_COOLDOWN, MUTE_TIME, JEDIS_PORT;
	public static List<String> CHECK_HIGH,CHECK_LOW,CHECK_SINGLE,CHECK_EXCEPTION;
	public static List<String> REPLACE_LIST;
	public static String JEDIS_ADDRESS, JEDIS_PASSWORD, KEEP_ALIVE_SERVER_NAME;

}
