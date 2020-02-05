package net.development.mitw.language.types;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.database.MongoDB;
import net.development.mitw.language.ILanguageData;
import net.development.mitw.player.MitwPlayer;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MongoLanguageData implements Listener, ILanguageData {

    @Getter
    @Setter
    private Plugin plugin;
    private MongoCollection<Document> players;

    public MongoLanguageData(final Plugin plugin) {
        this.plugin = plugin;
        this.players = Mitw.getInstance().getPlayerMongo().getDatabase().getCollection("player_languages");
    }

    public Document getPlayer(UUID uuid) {
        return this.players.find(Filters.eq("uuid", uuid.toString())).first();
    }

    public void replacePlayer(UUID uuid, Document document) {
        this.players.replaceOne(Filters.eq("uuid", uuid.toString()), document, Mitw.getInstance().getPlayerMongo().replaceOptions());
    }

    public String getLang(final Player p) {
        return getLang(p.getUniqueId());
    }

    public String getLang(final UUID uuid) {
        MitwPlayer mitwPlayer = MitwPlayer.getByUuid(uuid);

        if (mitwPlayer != null && mitwPlayer.isLoaded()) {
            return mitwPlayer.getLanguage();
        }

        Document document = this.getPlayer(uuid);

        String language;

        if (document == null) {
            language = DEFAULT_LANGUAGE;
        } else {

            language = document.getString("language");

            if (language == null || language.isEmpty() || !ArrayUtils.contains(LANGUAGES, language)) {
                language = DEFAULT_LANGUAGE;
            }
        }

        if (language == null || language.isEmpty() || !ArrayUtils.contains(LANGUAGES, language)) {
            return DEFAULT_LANGUAGE;
        }

        return language;
    }

    @Override
    public void setLang(Player player, String language, boolean first) {

        final ChangeLanguageEvent event = new ChangeLanguageEvent(player, language, first);
        plugin.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        if (!event.getLanguage().equals(language)) {
            language = event.getLanguage();
        }

        this.setLangData(player, language);
    }

    @Override
    public void setLangWithoutSave(Player p, String lang, boolean first) {
         MitwPlayer mitwPlayer = MitwPlayer.getByUuid(p.getUniqueId());

         if (mitwPlayer != null && mitwPlayer.isLoaded()) {
             mitwPlayer.setLanguage(lang);
         }
    }

    public void setLangData(Player player, String language) {
        if (language == null || language.isEmpty() || !ArrayUtils.contains(LANGUAGES, language)) {
            return;
        }

        Document document = new Document();
        document.put("uuid", player.getUniqueId().toString());
        document.put("language", language);

        this.replacePlayer(player.getUniqueId(), document);

        MitwPlayer mitwPlayer = MitwPlayer.getByUuid(player.getUniqueId());

        mitwPlayer.setLanguage(language);
    }

}
