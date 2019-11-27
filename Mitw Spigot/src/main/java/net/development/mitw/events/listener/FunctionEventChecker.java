package net.development.mitw.events.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionEventChecker {

    private Supplier<Boolean> nonPlayerChecker;
    private Function<Player, Boolean> playerChecker;

    public FunctionEventChecker playerOnly(Function<Player, Boolean> function) {
        this.playerChecker = function;
        return this;
    }

    public FunctionEventChecker nonPlayerOnly(Supplier<Boolean> function) {
        this.nonPlayerChecker = function;
        return this;
    }

    public boolean check(Event event) {
        Player player = getPlayerFromEvent(event);
        if (player != null && playerChecker != null) {
            return playerChecker.apply(((PlayerEvent) event).getPlayer());
        }
        if (nonPlayerChecker != null) {
            return nonPlayerChecker.get();
        }
        return true;
    }

    private static Player getPlayerFromEvent(Event event) {
        if (event instanceof PlayerEvent) {
            return ((PlayerEvent) event).getPlayer();
        }
        try {
            Method method = event.getClass().getDeclaredMethod("getPlayer");
            method.setAccessible(true);
            return (Player) method.invoke(event);
        } catch (Exception ex) {
            return null;
        }
    }

}
