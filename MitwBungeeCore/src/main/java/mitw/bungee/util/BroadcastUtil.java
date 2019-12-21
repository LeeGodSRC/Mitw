package mitw.bungee.util;

import com.google.common.collect.ImmutableList;
import mitw.bungee.Mitw;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BroadcastUtil {

    public static void alert(String message) {
        for (ProxiedPlayer player1 : ProxyServer.getInstance().getPlayers()) {
            Common.tell(player1, "§7[§6§lMitw§7] §f" + message);
        }

        Common.tell(ProxyServer.getInstance().getConsole(), "§7[§6§lMitw§7] §f" + message);
    }

    public static void broadcast(String server, boolean extra) {
        if (server.equals("uhc")) {
            broadcastUHC();
            return;
        }

        if (server.contains("sg")) {
            Mitw.INSTANCE.alertOnlySpecificServers("/sg " + server, "sg", "");
            return;
        }

        if (server.contains("meetup")) {
            Mitw.INSTANCE.alertOnlySpecificServers("/meetup " + server, "meetup", "§7(" + (extra ? "§bTeam" : "§eSolo") + "§7)");
            return;
        }

        Mitw.INSTANCE.alertOnlySpecificServers("/" + server, server.toLowerCase(), "");
    }

    public static void broadcastUHC() {
        ImmutableList<ProxiedPlayer> players = ImmutableList.copyOf(Mitw.INSTANCE.getProxy().getPlayers());
        final int size = players.size();
        final int diff = (int) Math.ceil(players.size() / 20D);

        Map<String, List<Object>> components = new HashMap<>();

        for (int i = 0, j = 0; i < size; i += diff) {

            final int start = i;
            final int end = i + diff;
            Mitw.INSTANCE.getProxy().getScheduler().schedule(Mitw.INSTANCE, () -> {
                for (int i1 = start; i1 < end; ++i1) {
                    if (i1 >= players.size())
                        return;

                    ProxiedPlayer player = players.get(i1);
                    if (player.isConnected()) {
                        String language = Mitw.INSTANCE.getLanguageData().getLang(player);
                        List<Object> text;
                        if (components.containsKey(language)) {
                            text = components.get(language);
                        } else {
                            text = Arrays.asList(
                                    Mitw.INSTANCE.getLanguage().translateArrays(player, "uhc"),
                                    Mitw.getJson("/uhc", player)
                            );
                            components.put(language, text);
                        }
                        Common.tell(player,"§7§m------------------------------------");
                        for (String string : ((List<String>)text.get(0))) {
                            Common.tell(player, string);
                        }
                        Common.tell(player, ((TextComponent)text.get(1)));
                        Common.tell(player,"§7§m------------------------------------");
                    }
                }
            }, ++j * 50, TimeUnit.MILLISECONDS);

        }
    }

}
