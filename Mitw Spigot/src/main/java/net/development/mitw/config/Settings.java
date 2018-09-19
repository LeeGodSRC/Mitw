package net.development.mitw.config;

import java.util.List;

public class Settings extends SimpleConfig{

	private static SimpleConfig config;

	private Settings(String fileName) {
		super(fileName);
		config = this;
	}

	public void loadVars() {
		IS_REPLACE_MODE = getBoolean("Replacemode.Enable");
		IS_NO_SAME_MESSAGE = getBoolean("AntiSpam.NoSameMessage");

		IS_BETTER_NICK = getBoolean("Hooks.Better-Nick");

		CHAT_COOLDOWN = getInt("AntiSpam.Cooldown");
		MUTE_TIME = getInt("MuteTime");

		CHECK_HIGH = getStringList("Checks.High");
		CHECK_LOW = getStringList("Checks.Low");
		CHECK_SINGLE = getStringList("Checks.Single");
		CHECK_EXCEPTION = getStringList("Checks.Exception");

		REPLACE_LIST = getStringList("Replacemode.ReplaceStringList");
	}

	public static void init() {
		final Settings s = new Settings("settings.yml");
		s.loadVars();
	}

	public static SimpleConfig getConfig() {
		return config;
	}

	public static boolean IS_REPLACE_MODE,IS_NO_SAME_MESSAGE;
	public static boolean IS_BETTER_NICK;
	public static int CHAT_COOLDOWN, MUTE_TIME;
	public static List<String> CHECK_HIGH,CHECK_LOW,CHECK_SINGLE,CHECK_EXCEPTION;
	public static List<String> REPLACE_LIST;

}
