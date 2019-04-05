package mitw.bungee.language;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public interface ILanguageData {

    String DEFAULT_LANGUAGE = "zh_tw";

    public default String getLang(ProxiedPlayer p) {
        return this.getLang(p.getUniqueId());
    }

    String getLang(UUID uuid);

    boolean hasLang(ProxiedPlayer p);

    void setLang(ProxiedPlayer p, String lang, boolean first);

    void setLangWithoutSave(ProxiedPlayer proxiedPlayer, String lang, boolean first);

    public static class ChangeLanguageEvent extends Event {

        private final ProxiedPlayer p;
        private String language;
        private boolean cancelled = false;
        private final boolean first;

        public ChangeLanguageEvent(final ProxiedPlayer p, final String language, final boolean first) {
            this.p = p;
            this.language = language;
            this.first = first;
        }

        public ProxiedPlayer getPlayer() {
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
    }
}
