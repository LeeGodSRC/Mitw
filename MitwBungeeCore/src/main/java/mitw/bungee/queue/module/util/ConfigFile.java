package mitw.bungee.queue.module.util;

import mitw.bungee.Mitw;
import mitw.bungee.queue.module.LobbyServer;
import mitw.bungee.queue.module.queue.Queue;
import mitw.bungee.queue.shared.Rank;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

public class ConfigFile {
    private Mitw plugin;
    private File file;
    private Configuration configuration;
    private boolean sendMoveMessage;

    public ConfigFile(final Mitw plugin) {
        this.sendMoveMessage = false;
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "queue.yml");
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        if (!this.file.exists()) {
            try {
                final InputStream in = plugin.getResourceAsStream("queue.yml");
                try {
                    Files.copy(in, this.file.toPath(), new CopyOption[0]);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            this.configuration = ConfigurationProvider.getProvider((Class) YamlConfiguration.class).load(new File(plugin.getDataFolder(), "queue.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loadLobbies();
        this.loadRanks();
        this.loadQueues();
    }

    public String getString(final String path) {
        return this.configuration.getString(path);
    }

    public int getInt(final String path) {
        return this.configuration.getInt(path);
    }

    public double getDouble(final String path) {
        return this.configuration.getDouble(path);
    }

    public boolean getBoolean(final String path) {
        return this.configuration.getBoolean(path);
    }

    public Collection<String> getSection(final String section) {
        return (Collection<String>) this.configuration.getSection(section).getKeys();
    }

    public List<String> getStringList(final String path) {
        return (List<String>) this.configuration.getStringList(path);
    }

    public void setObject(final String path, final Object obj) {
        this.configuration.set(path, obj);
        try {
            ConfigurationProvider.getProvider((Class) YamlConfiguration.class).save(this.configuration, new File(plugin.getDataFolder(), "queue.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLobbies() {
        this.getStringList("lobbies").stream().filter(l -> this.plugin.getProxy().getServerInfo(l) != null).forEach(l -> new LobbyServer(l, this.plugin.getProxy().getServerInfo(l)));
    }

    private void loadRanks() {
        for (final String rank : this.getSection("ranks")) {
            final String path = "ranks." + rank + ".";
            new Rank(rank, this.getString(String.valueOf(path) + "permission"), this.getInt(String.valueOf(path) + "priority"));
        }
    }

    private void loadQueues() {
        for (final String key : this.getSection("servers")) {
            final String path = "servers." + key + ".";
            if (this.plugin.getProxy().getServerInfo(key) == null) {
                System.err.println(String.valueOf(C.RED) + "[QueueManager] Failed to find bungee server for " + key + " queue!");
            } else {
                new Queue(key, this.plugin.getProxy().getServerInfo(key), this.getInt(String.valueOf(path) + "limit"), this.getDouble(String.valueOf(path) + "send-delay"));
                System.out.println("[QueueManager] Created queue for bungee server '" + key + "'");
            }
        }
    }

    public boolean isSendMoveMessage() {
        return this.sendMoveMessage;
    }
}
