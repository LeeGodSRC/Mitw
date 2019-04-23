package mitw.bungee.database;

import mitw.bungee.Mitw;
import mitw.bungee.config.impl.Config;
import mitw.bungee.util.FastUUID;

import java.util.UUID;

public class PlayerFlatFileData {

    private UUID uuid;

    public PlayerFlatFileData(UUID uuid) {
        this.uuid = uuid;
    }

    public Config load() {
        Config config = new Config(FastUUID.toString(uuid), Mitw.INSTANCE.getDataFolder().getAbsolutePath() + "/playerdata");
        return config;
    }

}
