package mitw.bungee.config.impl;

import mitw.bungee.Mitw;
import mitw.bungee.config.YamlConfiguration;
import mitw.bungee.util.FileUtil;
import lombok.Getter;

import java.io.File;
import java.io.IOException;

public class Config extends YamlConfiguration {

    @Getter
    private final String fileName;

    @Getter
    private final File file;

    public Config(final String name) {
        this(name, Mitw.INSTANCE.getDataFolder().getAbsolutePath());
    }

    public Config(final String name, String path) {
        this.fileName = name;

        final Mitw plugin = Mitw.INSTANCE;

        file = new File(path, name + ".yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!file.exists()) {
            FileUtil.saveResource(fileName + ".yml", false);
        }

        try {
            super.load(file);
            System.out.println("file " + fileName + ".yml has been created.");
        } catch (final Exception e) {
            System.out.println("an error happend on creating file " + fileName + ".yml");
            e.printStackTrace();
        }

    }

    public void save() {
        try {
            super.save(file);
        } catch (final IOException e) {
            System.out.println("an error happend on saving file " + fileName + ".yml");
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            super.load(file);
            System.out.println("file " + fileName + ".yml has been reloaded.");
        } catch (final Exception e) {
            System.out.println("an error happened on reloading file " + fileName + ".yml");
            e.printStackTrace();
        }
    }

}

