package mitw.bungee.config.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import mitw.bungee.Mitw;

public class General extends Config {

	@Getter
	private static General general;
	private Mitw main;

	public General(Mitw main) {
		super("General");
		general = this;
		this.main = main;
	}

	public static List<String> motd = new ArrayList<>();

	public static boolean REDIS_BUNGEE;
	public static int JEDIS_PORT;
	public static String JEDIS_ADDRESS, JEDIS_PASSWORD;

	public void setup() {

		for (final String str : getStringList("motd")) {
			motd.add(str);
		}

		addDefault("jedis.redisBungee", false);
		REDIS_BUNGEE = getBoolean("jedis.redisBungee", false);
		addDefault("jedis.address", "127.0.0.1");
		JEDIS_ADDRESS = getString("jedis.address", "127.0.0.1");
		addDefault("jedis.port", 6379);
		JEDIS_PORT = getInt("jedis.port", 6379);
		addDefault("jedis.password", "");
		JEDIS_PASSWORD = getString("jedis.password", "");

		if (JEDIS_ADDRESS == null) {
			JEDIS_ADDRESS = "127.0.0.1";
			JEDIS_PORT = 6379;
			JEDIS_PASSWORD = "52156";
		}

		save();
	}
}
