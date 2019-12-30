package net.development.mitw.language.types;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.language.ILanguageData;
import net.development.mitw.player.MitwPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class MongoLanguageData implements Listener, ILanguageData {

    @Getter
    @Setter
    private Plugin plugin;

    public MongoLanguageData(final Plugin plugin) {
        this.plugin = plugin;
    }

    public String getLang(final Player p) {
        return getLang(p.getUniqueId());
    }

    public String getLang(final UUID uuid) {
        MitwPlayer mitwPlayer = MitwPlayer.getByUuid(uuid);

        if (mitwPlayer == null) {
            return MitwPlayer.DEFAULT_LANGUAGE;
        }

        return mitwPlayer.getLanguage();
    }

    @Override
    public void setLang(Player player, String language, boolean first) {

        final ChangeLanguageEvent event = new ChangeLanguageEvent(player, language, first);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        if (!event.getLanguage().equals(language)) {
            language = event.getLanguage();
        }

        this.setLangData(player, language);
    }

    @Override
    public void setLangWithoutSave(Player p, String lang, boolean first) {
        this.setLangData(p, lang);
    }

    public void setLangData(Player player, String language) {
        MitwPlayer mitwPlayer = MitwPlayer.getByUuid(player.getUniqueId());

        if (mitwPlayer == null) {
            return;
        }

        mitwPlayer.setLanguage(language);
    }

}
