package net.development.mitw.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class LanguageLoadedEvent extends PlayerEvent {

    private static final HandlerList handlerlist = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerlist;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerlist;
    }

    @Getter
    private String language;

    public LanguageLoadedEvent(Player player, String language) {
        super(player);
        this.language = language;
    }

}
