package mitw.bungee.jedis.impl;

import com.google.common.collect.ImmutableList;
import mitw.bungee.Mitw;
import mitw.bungee.jedis.JedisSubscriptionHandler;
import mitw.bungee.util.Common;
import mitw.bungee.util.FastUUID;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MitwSubscriptionHandler implements JedisSubscriptionHandler {

    @Override
    public void handleMessage(JsonObject json) {
        String payload = json.get("payload").getAsString();

        JsonObject object = json.get("data").getAsJsonObject();
        String server;

        switch (payload) {
            case "LANGUAGE_CHANGED":

                UUID uuid = FastUUID.parseUUID(object.get("uuid").getAsString());
                String language = object.get("language").getAsString();

                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

                if (player == null) {
                    return;
                }

                Mitw.INSTANCE.getLanguageData().setLangWithoutSave(player, language, false);
                player.sendMessage(Mitw.INSTANCE.getLanguage().translate(player, "choose"));

                break;

            case "KEEP_ALIVE":

                server = object.get("server").getAsString();
                long time = object.get("time").getAsLong();

                Mitw.INSTANCE.getKeepAliveHandler().handleKeepAlive(server, time);

                break;

            case "BROADCAST_GAME":

                server = object.get("server").getAsString();
                boolean extra = false;
                if (object.has("extra")) {
                    extra = object.get("extra").getAsBoolean();
                }

                if (server.equals("uhc")) {
                    uhcAlert();
                    break;
                }

                if (server.contains("sg")) {
                    Mitw.INSTANCE.alertOnlySpecificServers("/sg " + server, "sg", "");
                    break;
                }

                if (server.contains("meetup")) {
                    Mitw.INSTANCE.alertOnlySpecificServers("/meetup " + server, "meetup", "§7(" + (extra ? "§bTeam" : "§eSolo") + "§7)");
                    break;
                }

                Mitw.INSTANCE.alertOnlySpecificServers("/" + server, server.toLowerCase(), "");
                break;

            case "BUNGEE_ALERT":
                String message = object.get("message").getAsString();

                for (ProxiedPlayer player1 : ProxyServer.getInstance().getPlayers()) {
                    Common.tell(player1, "§7[§6§lMitw§7] §f" + message);
                }

                Common.tell(ProxyServer.getInstance().getConsole(), "§7[§6§lMitw§7] §f" + message);
                break;

        }
    }

    private void uhcAlert() {
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
