package mitw.bungee.language.types;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import mitw.bungee.Mitw;
import mitw.bungee.language.ILanguageData;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MongoLanguageData implements Listener, ILanguageData {

    @Getter
    @Setter
    private Plugin plugin;

    private static Map<UUID, String> playerLangs = new HashMap<>();

    public MongoLanguageData(final Plugin plugin) {
        this.plugin = plugin;
    }

    public String getLang(final ProxiedPlayer p) {
        return getLang(p.getUniqueId());
    }

    public String getLang(final UUID uuid) {
        if (playerLangs.containsKey(uuid)) {
            return playerLangs.get(uuid);
        }

        Document document = Mitw.INSTANCE.getMongo().getPlayer(uuid);

        String language;

        if (document == null) {
            language = DEFAULT_LANGUAGE;
        } else {

            language = document.getString("language");

            if (language == null || language.isEmpty() || !ArrayUtils.contains(LANGUAGES, language)) {
                language = DEFAULT_LANGUAGE;
            }
        }

        playerLangs.put(uuid, language);

        return language;
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

        if (language == null || language.isEmpty() || !ArrayUtils.contains(LANGUAGES, language)) {
            language = DEFAULT_LANGUAGE;
        }

        Document document = new Document();
        document.put("uuid", player.getUniqueId().toString());
        document.put("language", language);

        Mitw.INSTANCE.getMongo().replacePlayer(player.getUniqueId(), document);

        playerLangs.put(player.getUniqueId(), language);
    }

}
