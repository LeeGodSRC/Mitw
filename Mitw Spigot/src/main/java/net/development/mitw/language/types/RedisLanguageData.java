package net.development.mitw.language.types;

import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.language.ILanguageData;
import net.development.mitw.player.MitwPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RedisLanguageData implements Listener, ILanguageData {

    @Getter
    @Setter
    private Plugin plugin;

    public RedisLanguageData(final Plugin plugin) {
        this.plugin = plugin;
    }

    public String getLang(final Player p) {
        return getLang(p.getUniqueId());
    }

    public String getLang(final UUID uuid) {
        MitwPlayer mitwPlayer = MitwPlayer.getByUuid(uuid);

        if (mitwPlayer != null && mitwPlayer.isLoaded()) {
            return mitwPlayer.getLanguage();
        }

        return Mitw.getInstance().getMitwJedis().runCommand(jedis -> jedis.hget("language", uuid.toString()));
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
        Mitw.getInstance().getMitwJedis().runCommand(jedis -> jedis.hset("language", player.getUniqueId().toString(), language));

        MitwPlayer mitwPlayer = MitwPlayer.getByUuid(player.getUniqueId());

        mitwPlayer.setLanguage(language);
    }

}
