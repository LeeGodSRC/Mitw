package mitw.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.common.io.ByteStreams;

import mitw.Bungee;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class YamlManagers {
	Bungee main;
	File Settings;
	public static File GeneralPath;
	public static Configuration General;

	public YamlManagers(Bungee main) {
		this.main = main;
	}

	public static List<UUID> Ignores = new ArrayList<>();
	public static List<String> motd = new ArrayList<>();

	public void SetUp() {
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
			General = ConfigurationProvider.getProvider(YamlConfiguration.class).load(GeneralPath);
		} catch (final IOException e) {
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
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(General, GeneralPath);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
