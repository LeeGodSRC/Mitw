package net.development.mitw.hooks;

import lombok.Getter;
import me.efla.API.API;
import me.efla.Main;
import me.efla.connection.Communication;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class eFlameHook {

    @Getter
    private static boolean hooked;

    public static void tryHook() {
        hooked = Bukkit.getPluginManager().getPlugin("eFlame") != null;
    }

    public static void checkPlayer(Player player) {
        if (isHooked()) {
            API.sendCheckRequest(player, true);
        }
    }

    public static void uncheckPlayer(Player player) {
        if (isHooked()) {
            if (Main.lista.contains(player.getName())) {
                Communication.removePlayer(player.getName());
            }
        }
    }

}
