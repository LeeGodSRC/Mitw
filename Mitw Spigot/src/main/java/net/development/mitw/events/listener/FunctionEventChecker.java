package net.development.mitw.events.listener;

import net.development.mitw.utils.Entry;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionEventChecker {

    private Supplier<Entry<Boolean, ?>> nonPlayerChecker;
    private Function<Player, Entry<Boolean, ?>> playerChecker;
    private Map<Class<? extends Event>, Function<Event, Player>> specialGetPlayer = new HashMap<>();

    public FunctionEventChecker playerOnly(Function<Player, Entry<Boolean, ?>> function) {
        this.playerChecker = function;
        return this;
    }

    public FunctionEventChecker nonPlayerOnly(Supplier<Entry<Boolean, ?>> function) {
        this.nonPlayerChecker = function;
        return this;
    }

    public FunctionEventChecker getPlayer(Class<? extends Event> eventClass, Function<Event, Player> function) {
        this.specialGetPlayer.put(eventClass, function);
        return this;
    }

    public Entry<Boolean, ?> check(Event event) {
        Player player = getPlayerFromEvent(event);
        if (player != null && playerChecker != null) {
            return playerChecker.apply(player);
        }
        if (nonPlayerChecker != null) {
            return nonPlayerChecker.get();
        }
        return new Entry<>(true, null);
    }

    private Player getPlayerFromEvent(Event event) {
        if (specialGetPlayer.containsKey(event.getClass())) {
            return specialGetPlayer.get(event.getClass()).apply(event);
        }
        if (event instanceof PlayerEvent) {
            return ((PlayerEvent) event).getPlayer();
        }
        try {
            for (Method method : event.getClass().getDeclaredMethods()) {
                if (Player.class.isAssignableFrom(method.getReturnType())) {
                    method.setAccessible(true);
                    return (Player) method.invoke(event);
                }
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

}
