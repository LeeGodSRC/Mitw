package net.development.mitw.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ConfigCursor {

    private final Configuration fileConfig;
    @Setter private String path;

    public boolean exists() {
        return this.exists(null);
    }

    public boolean exists(String path) {
        return this.fileConfig.contains(this.path + (path == null ? "" : "." + path));
    }

    public Set<String> getKeys() {
        return this.getKeys(null);
    }

    public Set<String> getKeys(String path) {
        return this.fileConfig.getConfigurationSection(this.path + (path == null ? "" : "." + path))
                .getKeys(false);
    }

    public String getString(String path) {
        return this.fileConfig.getString((this.path == null ? "" : this.path + ".") + path);
    }

    public boolean getBoolean(String path) {
        return this.fileConfig.getBoolean((this.path == null ? "" : this.path + ".") + "." + path);
    }

    public int getInt(String path) {
        return this.fileConfig.getInt((this.path == null ? "" : this.path + ".") + "." + path);
    }

    public long getLong(String path) {
        return this.fileConfig.getLong((this.path == null ? "" : this.path + ".") + "." + path);
    }

    public List<String> getStringList(String path) {
        return this.fileConfig.getStringList((this.path == null ? "" : this.path + ".") + "." + path);
    }

    public UUID getUuid(String path) {
        return UUID.fromString(this.fileConfig.getString(this.path + "." + path));
    }

    public World getWorld(String path) {
        return Bukkit.getWorld(this.fileConfig.getString(this.path + "." + path));
    }

    public void set(Object value) {
        this.set(null, value);
    }

    public void set(String path, Object value) {
        this.fileConfig.set(this.path + (path == null ? "" : "." + path), value);
    }

    public void save() {
        this.fileConfig.save();
    }

}