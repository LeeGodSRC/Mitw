package net.development.mitw.config;

import java.util.List;

public class Security extends SimpleConfig {

	public Security(String fileName) {
		super(fileName);
	}

	public void loadVar() {
		KICK_MESSAGE = getString("OnlyProxyJoin.KickMessage", "?");
		ALLOW_IPS = getStringList("OnlyProxyJoin.AllowIP");
	}

	public static void init() {
		final Security sql = new Security("Security.yml");
		sql.loadVar();
	}

	public static List<String> ALLOW_IPS;
	public static String KICK_MESSAGE;

}
