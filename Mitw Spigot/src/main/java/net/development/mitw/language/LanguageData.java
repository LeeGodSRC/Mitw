package net.development.mitw.language;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.development.mitw.Mitw;
import net.development.mitw.json.JsonChain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.utils.FastUUID;

public class LanguageData implements Listener {

    static final String DEFAULT_LANGUAGE = "zh_tw";

    private static Map<UUID, String> playerLangs = new HashMap<>();

    @Getter
    @Setter
    private Plugin plugin;
    @Getter
    @Setter
    private LanguageSQLConnection conn;

    private final ExecutorService langExecutor = Executors.newSingleThreadExecutor();

    public LanguageData(final Plugin plugin, final LanguageSQLConnection conn) {
        this.plugin = plugin;
        this.conn = conn;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        conn.connect();
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

    public void setLangRedis(Player player, String language) {
        Mitw.getInstance().getMitwJedis().write("LANGUAGE_CHANGED", new JsonChain()
                .addProperty("uuid", FastUUID.toString(player.getUniqueId())).addProperty("language", language).get());
        setLangSQL(player, language);
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
            setLangSQL(player, language);
        }
    }

    public void setLangSQL(Player player, String language) {
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
        final Player p = e.getPlayer();
        langExecutor.execute(() -> {
            if (hasLang(p)) {
                playerLangs.put(p.getUniqueId(), getLang(p));
            } else {
                setLang(p, DEFAULT_LANGUAGE, true, true);
            }
        });
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        langExecutor.execute(() -> {
            setLang(p, true);
            playerLangs.remove(p.getUniqueId());
        });
    }

    public static class ChangeLanguageEvent extends Event {

        private static final HandlerList handlerlist = new HandlerList();

        private final Player p;
        private String language;
        private boolean cancelled = false;
        private final boolean first;

        public ChangeLanguageEvent(final Player p, final String language, final boolean first) {
            this.p = p;
            this.language = language;
            this.first = first;
        }

        public Player getPlayer() {
            return p;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(final String language) {
            this.language = language;
        }

        public boolean isFirstSet() {
            return first;
        }

        public boolean isCancelled() {
            return cancelled;
        }

        public void setCancelled(final boolean cancel) {
            cancelled = cancel;
        }

        @Override
        public HandlerList getHandlers() {
            return handlerlist;
        }

        public static HandlerList getHandlerList() {
            return handlerlist;
        }
    }

}
