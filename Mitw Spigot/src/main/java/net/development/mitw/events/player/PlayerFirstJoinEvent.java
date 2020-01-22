package net.development.mitw.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerFirstJoinEvent extends PlayerEvent {

    private static final HandlerList handlerlist = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerlist;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerlist;
    }

    public PlayerFirstJoinEvent(Player player) {
        super(player);
    }
}
