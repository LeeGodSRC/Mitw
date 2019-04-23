package mitw.bungee.config.impl;

import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import mitw.bungee.Mitw;
import mitw.bungee.config.FileConfiguration;
import mitw.bungee.config.YamlConfiguration;
import com.google.common.io.ByteStreams;
import mitw.bungee.config.impl.Config;

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

	public static int JEDIS_PORT;
	public static String JEDIS_ADDRESS, JEDIS_PASSWORD;

	public void setup() {

		for (final String str : getStringList("motd")) {
			motd.add(str);
		}

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
