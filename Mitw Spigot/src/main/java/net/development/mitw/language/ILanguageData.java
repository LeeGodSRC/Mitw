package net.development.mitw.language;

import net.development.mitw.Mitw;
import net.development.mitw.json.JsonChain;
import net.development.mitw.utils.FastUUID;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.UUID;

public interface ILanguageData {

    String DEFAULT_LANGUAGE = "zh_tw";

    public default String getLang(Player p) {
        return this.getLang(p.getUniqueId());
    }

    String getLang(UUID uuid);

    boolean hasLang(Player p);

    void setLang(Player p, String lang, boolean first);

    void setLangWithoutSave(Player p, String lang, boolean first);

    void setLangData(Player player, String language);

    default void sendLangRedis(Player player, String language) {
        Mitw.getInstance().getMitwJedis().write("LANGUAGE_CHANGED", new JsonChain()
                .addProperty("uuid", FastUUID.toString(player.getUniqueId())).addProperty("language", language).get());
        setLangData(player, language);
    }

    public static class ChangeLanguageEvent extends PlayerEvent {

        private static final HandlerList handlerlist = new HandlerList();

        private String language;
        private boolean cancelled = false;
        private final boolean first;

        public ChangeLanguageEvent(final Player p, final String language, final boolean first) {
            super(p);
            this.language = language;
            this.first = first;
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
