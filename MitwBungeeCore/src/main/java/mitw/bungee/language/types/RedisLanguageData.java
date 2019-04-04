package mitw.bungee.language.types;

import mitw.bungee.language.ILanguageData;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class RedisLanguageData implements ILanguageData {

    @Override
    public String getLang(UUID uuid) {
        return null;
    }

    @Override
    public void setLang(ProxiedPlayer p, boolean sql) {

    }
}
