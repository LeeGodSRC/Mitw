package net.development.mitw.language;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.utils.ClassUtil;
import net.development.mitw.utils.RV;
import net.development.mitw.utils.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class NewLanguageAPI {

    @Getter
    @Setter
    private Map<String, List<String>> savedMessages = new HashMap<>();

    @Getter
    @Setter
    private Plugin plugin;
    @Getter
    private final LanguageData languageData;
    @Getter
    private Map<String, FileConfiguration> languageFileMap = new HashMap<>();

    public NewLanguageAPI(final Plugin plugin, final LanguageData languageData) {
        this.plugin = plugin;
        this.languageData = languageData;

        try {

            String[] list = ClassUtil.getResourceListing(plugin.getClass(), "languages/");

            String next;
            for (int i = 0; i < list.length; i++) {
                next = list[i];
                plugin.getServer().getConsoleSender().sendMessage(next);
                if (!next.endsWith(".lang")) {
                    continue;
                }
                FileConfiguration data = YamlConfiguration.loadConfiguration(plugin.getResource("languages/" + next));
                this.languageFileMap.put(data.getString("lang"), data);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void send(final Player p, final String translateMessage) {
        p.sendMessage(translate(p, translateMessage));
    }

    public void send(final Player p, final String translateMessage, final RV... replaceValue) {
        p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue));
    }

    public void send(final String translateMessage) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(translate(p, translateMessage)));
    }

    public void send(final String translateMessage, final RV... replaceValue) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
    }

    public void send(final Set<Player> player, final String translateMessage) {
        player.forEach(p -> p.sendMessage(translate(p, translateMessage)));
    }

    public void send(final Set<Player> player, final String translateMessage, final RV... replaceValue) {
        player.forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
    }

    public void send(final List<Player> player, final String translateMessage) {
        player.forEach(p -> p.sendMessage(translate(p, translateMessage)));
    }

    public void send(final List<Player> player, final String translateMessage, final RV... replaceValue) {
        player.forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
    }

    public void send2(final Set<UUID> player, final String translateMessage) {
        send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage);
    }

    public void send2(final Set<UUID> player, final String translateMessage, final RV... replaceValue) {
        send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage, replaceValue);
    }

    public void send2(final List<UUID> player, final String translateMessage) {
        send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage);
    }

    public void send2(final List<UUID> player, final String translateMessage, final RV... replaceValue) {
        send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage, replaceValue);
    }

    public String translate(final Player p, final String ofrom, final RV... replaceValues) {
        return StringUtil.replace(translate(p, ofrom), replaceValues);
    }

    public String translate(final Player p, final String from) {
        final String lang = languageData.getLang(p);
        if (savedMessages.containsKey(from)) {
            return savedMessages.get(from).get(0);
        } else {

            FileConfiguration config = this.languageFileMap.get(lang);

            if (config == null) {
                Bukkit.getConsoleSender().sendMessage("cannot find the language: " + lang);
                return "null";
            }

            String to = config.getString(from);
            boolean found = false;
            if (to == null) {
                Bukkit.getConsoleSender().sendMessage("cannot find " + from + " in config " + lang);
                return "null";
            } else {
                to = ChatColor.translateAlternateColorCodes('&', to);
                savedMessages.put(from, Arrays.asList(to));
            }

            return to;
        }
    }

    public List<String> translateArrays(final Player p, final String ofrom, final RV... replaceValues) {
        return translateArrays(p, ofrom).stream().map(string -> StringUtil.replace(string, replaceValues)).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public List<String> translateArrays(final Player p, final String from) {
        final String lang = languageData.getLang(p);
        if (savedMessages.containsKey(from))
            return savedMessages.get(from);
        else {
            FileConfiguration config = this.languageFileMap.get(lang);

            if (config == null) {
                Bukkit.getConsoleSender().sendMessage("cannot find the language: " + lang);
                return Arrays.asList("null");
            }

            List<String> to = config.getStringList(from);
            boolean found = false;
            if (to == null) {
                return Arrays.asList("null");
            } else {
                found = true;
            }

            if (found) {
                for (int i = 0; i < to.size(); i++) {
                    to.set(i, ChatColor.translateAlternateColorCodes('&', to.get(i)));
                }
                savedMessages.put(from, to);
            }
            return to;
        }
    }
}
