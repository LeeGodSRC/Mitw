package mitw.bungee.language.types;

import lombok.Getter;
import lombok.Setter;
import mitw.bungee.Mitw;
import mitw.bungee.language.ILanguageData;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisLanguageData implements Listener, ILanguageData {

    @Getter
    @Setter
    private Plugin plugin;

    private static Map<UUID, String> playerLangs = new HashMap<>();

    public RedisLanguageData(final Plugin plugin) {
        this.plugin = plugin;
    }

    public String getLang(final ProxiedPlayer p) {
        return getLang(p.getUniqueId());
    }

    public String getLang(final UUID uuid) {
        if (playerLangs.containsKey(uuid)) {
            return playerLangs.get(uuid);
        }

        return Mitw.INSTANCE.getMitwJedis().runCommand(jedis -> jedis.hget("language", uuid.toString()));
    }

    @Override
    public void setLang(ProxiedPlayer player, String language, boolean first) {

        final ChangeLanguageEvent event = new ChangeLanguageEvent(player, language, first);
        plugin.getProxy().getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        if (!event.getLanguage().equals(language)) {
            language = event.getLanguage();
        }

        this.setLangData(player, language);
    }

    @Override
    public void setLangWithoutSave(ProxiedPlayer p, String lang, boolean first) {
        playerLangs.put(p.getUniqueId(), lang);
    }

    public void setLangData(ProxiedPlayer player, String language) {
        Mitw.INSTANCE.getMitwJedis().runCommand(jedis -> jedis.hset("language", player.getUniqueId().toString(), language));
    }

}
