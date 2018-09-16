package net.development.mitw.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ConfigurationReloadEvent extends Event{
	
	private static final HandlerList handlerlist = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerlist;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }

}
