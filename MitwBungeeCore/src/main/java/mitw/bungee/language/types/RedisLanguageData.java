package mitw.bungee.language.types;

import lombok.Getter;
import lombok.Setter;
import mitw.bungee.Mitw;
import mitw.bungee.jedis.MitwJedis;
import mitw.bungee.language.ILanguageData;
import mitw.bungee.language.LanguageSQLConnection;
import mitw.bungee.util.FastUUID;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisLanguageData implements Listener, ILanguageData {

    private static Map<UUID, String> playerLangs = new HashMap<>();

    @Getter
    @Setter
    private Plugin plugin;
    @Getter @Setter private MitwJedis jedis;

    private final ExecutorService langExecutor = Executors.newSingleThreadExecutor();

    public RedisLanguageData(final Plugin plugin, final MitwJedis jedis) {
        this.plugin = plugin;
        this.jedis = jedis;
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @Override
    public String getLang(final UUID uuid) {
        if (playerLangs.containsKey(uuid))
            return playerLangs.get(uuid);
        return jedis.runCommand(redis -> redis.hget("language", FastUUID.toString(uuid)));
    }

    @Override
    public boolean hasLang(final ProxiedPlayer p) {
        if (playerLangs.containsKey(p.getUniqueId()))
            return true;
        return hasLangRedis(p);
    }

    @Override
    public void setLangWithoutSave(ProxiedPlayer proxiedPlayer, String lang, boolean first) {
        this.setLang(proxiedPlayer, lang, first, false);
    }

    public boolean hasLangRedis(final ProxiedPlayer p) {
        return jedis.runCommand(redis -> redis.hexists("language", FastUUID.toString(p.getUniqueId())));
    }

    @Override
    public void setLang(final ProxiedPlayer p, String language, boolean first) {
        this.setLang(p, language, first, true);
    }

    public void setLang(final ProxiedPlayer p, String language, boolean first, boolean redis) {
        final ChangeLanguageEvent event = new ChangeLanguageEvent(p, language, first);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled())
            return;
        language  = event.getLanguage();
        playerLangs.put(p.getUniqueId(), language);
        if(!redis)
            return;
        jedis.runCommand(jedis -> jedis.hset("language", FastUUID.toString(p.getUniqueId()), playerLangs.get(p.getUniqueId())));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(final PostLoginEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        langExecutor.execute(() -> {
            if (hasLang(p)) {
                playerLangs.put(p.getUniqueId(), getLang(p));
            } else {
                setLang(p, DEFAULT_LANGUAGE, true, true);
            }
        });
    }

    @EventHandler
    public void onQuit(final ServerDisconnectEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        playerLangs.remove(p.getUniqueId());
    }
}
