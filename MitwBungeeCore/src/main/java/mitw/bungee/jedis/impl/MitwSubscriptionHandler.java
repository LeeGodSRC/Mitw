package mitw.bungee.jedis.impl;

import mitw.bungee.Mitw;
import mitw.bungee.events.PlayerEntryAddEvent;
import mitw.bungee.jedis.JedisSubscriptionHandler;
import mitw.bungee.util.BroadcastUtil;
import mitw.bungee.util.FastUUID;
import com.google.gson.JsonObject;
import net.development.mitw.common.JedisPackets;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class MitwSubscriptionHandler implements JedisSubscriptionHandler {

    @Override
    public void handleMessage(JsonObject json) {
        String payload = json.get("payload").getAsString();

        JsonObject object = json.get("data").getAsJsonObject();
        String server;

        switch (payload) {
            case JedisPackets.LANGUAGE_CHANGED:

                UUID uuid = FastUUID.parseUUID(object.get("uuid").getAsString());
                String language = object.get("language").getAsString();

                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

                if (player == null) {
                    return;
                }

                Mitw.INSTANCE.getLanguageData().setLangWithoutSave(player, language, false);
                player.sendMessage(Mitw.INSTANCE.getLanguage().translate(player, "choose"));

                break;

            case JedisPackets.KEEP_ALIVE:

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

                BroadcastUtil.broadcast(server, extra);
                break;

            case "BUNGEE_ALERT":
                BroadcastUtil.alert(object.get("message").getAsString());
                break;

            case JedisPackets.LITE_BANS_ENTRY_ADD:

                uuid = UUID.fromString(object.get("uuid").getAsString());
                String entry = object.get("entry").getAsString();
                player = ProxyServer.getInstance().getPlayer(uuid);

                if (player == null) {
                    break;
                }

                ProxyServer.getInstance().getPluginManager().callEvent(new PlayerEntryAddEvent(player, entry));
                break;

        }
    }

}
