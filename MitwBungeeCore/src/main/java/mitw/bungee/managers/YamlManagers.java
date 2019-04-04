package mitw.bungee.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mitw.bungee.Mitw;
import mitw.bungee.config.FileConfiguration;
import mitw.bungee.config.YamlConfiguration;
import com.google.common.io.ByteStreams;

public class YamlManagers {
	Mitw main;
	File Settings;
	public static File GeneralPath;
	public static FileConfiguration General;

	public YamlManagers(Mitw main) {
		this.main = main;
	}

	public static List<UUID> Ignores = new ArrayList<>();
	public static List<String> motd = new ArrayList<>();

	public void setup() {
		if (!this.main.getDataFolder().exists()) {
			this.main.getDataFolder().mkdir();
		}
		this.Settings = new File(this.main.getDataFolder(), "Settings");
		if (!this.Settings.exists()) {
			this.Settings.mkdir();
		}
		GeneralPath = new File(this.main.getDataFolder(), "Settings" + File.separator + "General.yml");
		if (!GeneralPath.exists()) {
			final InputStream in = getClass().getResourceAsStream("General.yml");
			try {
				final FileOutputStream out = new FileOutputStream(GeneralPath);
				ByteStreams.copy(in, out);

				in.close();
				out.close();
			} catch (final FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		try {
			General = new YamlConfiguration();
			General.load(GeneralPath);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		for (final String str : General.getStringList("ignore")) {
			Ignores.add(UUID.fromString(str));
		}

		for (final String str : General.getStringList("motd")) {
			motd.add(str);
		}
	}

	public static void saveConfig() {
		try {
			General.save(GeneralPath);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
