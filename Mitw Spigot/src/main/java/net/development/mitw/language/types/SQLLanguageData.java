package net.development.mitw.language.types;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.development.mitw.events.LanguageLoadedEvent;
import net.development.mitw.language.ILanguageData;
import net.development.mitw.player.database.PlayerDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.utils.FastUUID;

public class SQLLanguageData implements Listener, ILanguageData {

    static final String DEFAULT_LANGUAGE = "zh_tw";

    private static Map<UUID, String> playerLangs = new HashMap<>();

    @Getter
    @Setter
    private Plugin plugin;
    @Getter
    @Setter
    private PlayerDatabase conn;

    public SQLLanguageData(final Plugin plugin, final PlayerDatabase conn) {
        this.plugin = plugin;
        this.conn = conn;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public String getLang(final Player p) {
        return getLang(p.getUniqueId());
    }

    public String getLang(final UUID uuid) {
        if (playerLangs.containsKey(uuid))
            return playerLangs.get(uuid);
        return conn.getSqlTable().executeSelect("uuid = ?")
                .dataSource(conn.getDatabase().getDataSource())
                .statement(s -> s.setString(1, FastUUID.toString(uuid)))
                .resultNext(r -> {
                    return r.getString("lang");
                }).run("", "");
    }

    public boolean hasLang(final Player p) {
        if (playerLangs.containsKey(p.getUniqueId()))
            return true;
        return hasLangSQL(p);
    }

    public boolean hasLangSQL(final Player p) {
        return conn.getSqlTable().executeSelect("uuid = ?")
                .dataSource(conn.getDatabase().getDataSource())
                .statement(s -> s.setString(1, p.getUniqueId().toString()))
                .resultNext(r -> true)
                .run(false, false);
    }

    public void setLang(final Player p, final boolean sql) {
        if (playerLangs.containsKey(p.getUniqueId())) {
            setLang(p, playerLangs.get(p.getUniqueId()), sql, false);
        } else {
            setLang(p, DEFAULT_LANGUAGE, sql, false);
        }
    }

    @Override
    public void setLang(Player p, String lang, boolean first) {
        this.setLang(p, lang, true, first);
    }

    @Override
    public void setLangWithoutSave(Player p, String lang, boolean first) {
        this.setLang(p, lang, false, first);
    }

    public void setLang(final Player player, String language, final boolean sql, final boolean first) {
        final ChangeLanguageEvent event = new ChangeLanguageEvent(player, language, first);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        if (!event.getLanguage().equals(language)) {
            language = event.getLanguage();
        }
        playerLangs.put(player.getUniqueId(), language);
        if (sql) {
            setLangData(player, language);
        }
    }

    public void setLangData(Player player, String language) {
        if (hasLangSQL(player)) {
            conn.getSqlTable().executeUpdate("UPDATE `MitwLang` SET `lang` = ? WHERE `uuid` = ?;")
                    .dataSource(conn.getDatabase().getDataSource()).statement(s -> {
                s.setString(1, language);
                s.setString(2, FastUUID.toString(player.getUniqueId()));
            }).run();
        } else {
            conn.getSqlTable().executeInsert("?, ?")
                    .dataSource(conn.getDatabase().getDataSource())
                    .statement(s -> {
                        s.setString(1, FastUUID.toString(player.getUniqueId()));
                        s.setString(2, language);
                    }).run();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(final PlayerLoginEvent e) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            Player p = e.getPlayer();
            String language;
            if (hasLang(p)) {
                language = playerLangs.put(p.getUniqueId(), getLang(p));
            } else {
                language = DEFAULT_LANGUAGE;
                setLang(p, DEFAULT_LANGUAGE, true, true);
            }
            Bukkit.getPluginManager().callEvent(new LanguageLoadedEvent(p, language));
        });
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        playerLangs.remove(p.getUniqueId());
    }

}
