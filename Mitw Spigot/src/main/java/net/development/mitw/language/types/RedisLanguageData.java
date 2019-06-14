package net.development.mitw.language.types;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.jedis.MitwJedis;
import net.development.mitw.language.ILanguageData;
import net.development.mitw.utils.FastUUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisLanguageData implements Listener, ILanguageData {

    private static Map<UUID, String> playerLangs = new HashMap<>();

    @Getter
    @Setter
    private Plugin plugin;
    @Getter
    @Setter
    private MitwJedis jedis;

    public RedisLanguageData(final Plugin plugin, final MitwJedis jedis) {
        this.plugin = plugin;
        this.jedis = jedis;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getLang(final UUID uuid) {
        if (playerLangs.containsKey(uuid))
            return playerLangs.get(uuid);
        return jedis.runCommand(redis -> redis.hget("language", FastUUID.toString(uuid)));
    }

    @Override
    public boolean hasLang(final Player p) {
        if (playerLangs.containsKey(p.getUniqueId()))
            return true;
        return hasLangRedis(p);
    }

    public boolean hasLangRedis(final Player p) {
        return jedis.runCommand(redis -> redis.hexists("language", FastUUID.toString(p.getUniqueId())));
    }

    @Override
    public void setLang(final Player p, String language, boolean first) {
        this.setLang(p, language, first, true);
    }

    @Override
    public void setLangWithoutSave(Player p, String lang, boolean first) {
        this.setLang(p, lang, first, false);
    }

    @Override
    public void setLangData(Player player, String language) {
        jedis.runCommand(jedis -> jedis.hset("language", FastUUID.toString(player.getUniqueId()), language));
    }

    public void setLang(final Player p, String language, boolean first, boolean redis) {
        final ChangeLanguageEvent event = new ChangeLanguageEvent(p, language, first);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        language = event.getLanguage();
        playerLangs.put(p.getUniqueId(), language);
        if (!redis)
            return;
        setLangData(p, language);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(final AsyncPlayerLoginEvent e) {
        final Player p = e.getPlayer();
        if (hasLang(p)) {
            playerLangs.put(p.getUniqueId(), getLang(p));
        } else {
            setLang(p, DEFAULT_LANGUAGE, true, true);
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        playerLangs.remove(p.getUniqueId());
    }
}
