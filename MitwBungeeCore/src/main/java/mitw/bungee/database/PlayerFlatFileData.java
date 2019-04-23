package mitw.bungee.database;

import mitw.bungee.Mitw;
import mitw.bungee.config.Configuration;
import mitw.bungee.config.FileConfiguration;
import mitw.bungee.config.YamlConfiguration;
import mitw.bungee.config.impl.Config;
import mitw.bungee.util.FastUUID;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerFlatFileData {

    private UUID uuid;
    private File file;
    private FileConfiguration config;

    public PlayerFlatFileData(UUID uuid) {
        this.uuid = uuid;
    }

    public Configuration load() {
        File folder = new File(Mitw.INSTANCE.getDataFolder(), "playerdata");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        file = new File(Mitw.INSTANCE.getDataFolder(), "playerdata" + File.separator + FastUUID.toString(uuid) + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
