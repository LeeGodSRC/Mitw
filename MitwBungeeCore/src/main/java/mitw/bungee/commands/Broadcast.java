package mitw.bungee.commands;

import com.google.common.collect.ImmutableList;
import mitw.bungee.Mitw;
import mitw.bungee.util.Common;
import com.ilummc.tlib.util.Strings;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Broadcast extends Command {

    public Broadcast() {
        super("broadcast", "mitw.admin", "ba");
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0)
            return;
        final String serverName = args[0].toLowerCase();
        if (serverName.equals("uhc")) {
            UHCAlert();
            return;
        }
        if (serverName.contains("sg")) {
            sgAlert(sender, serverName);
            return;
        }
        if (serverName.contains("meetup") && isBoolean(args[1])) {
            meetupAlert(sender, serverName, Boolean.parseBoolean(args[1]));
            return;
        }
        Mitw.INSTANCE.broadcastOnlySpecificServers("alert", "/" + serverName, serverName.toLowerCase(), "");
        Common.tell(sender, "§a成功發送公告");
        return;
    }

    private void meetupAlert(final CommandSender sender, final String serverName, final boolean team) {
        Mitw.INSTANCE.broadcastOnlySpecificServers("alert", "/meetup" + serverName, "meetup", "§7(" + (team ? "§bTeam" : "§eSolo") + "§7)");
    }

    public void UHCAlert() {
        ImmutableList<ProxiedPlayer> players = ImmutableList.copyOf(Mitw.INSTANCE.getProxy().getPlayers());
        final int size = players.size();
        final int diff = (int) Math.ceil(players.size() / 20D);

        Map<String, List<Object>> components = new HashMap<>();

        for (int i = 0, j = 0; i < size; i += diff) {

            if (i >= size)
                return;

            // Some shit for the task
            final int start = i;
            final int end = i + diff;
            Mitw.INSTANCE.getProxy().getScheduler().schedule(Mitw.INSTANCE, () -> {
                for (int i1 = start; i1 < end; ++i1) {
                    // Overshot
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
                                    genJSONMsg("/uhc", player)
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

    public void sgAlert(final CommandSender sender, final String server) {
        Mitw.INSTANCE.broadcastOnlySpecificServers("alert", "/sg " + server, "sg", "");
    }

    public static TextComponent genJSONMsg(final String cmd, final ProxiedPlayer player) {
        final TextComponent msg = new TextComponent(Mitw.INSTANCE.getLanguage().translate(player, "clickjoin"));
        msg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, cmd));
        return msg;
    }

    public boolean isBoolean(final String string) {
        try {
            Boolean.parseBoolean(string);
        } catch (final Exception e) {
            return false;
        }
        return true;
    }

}
