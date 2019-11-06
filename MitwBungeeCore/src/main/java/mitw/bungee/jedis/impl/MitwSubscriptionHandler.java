package mitw.bungee.jedis.impl;

import mitw.bungee.Mitw;
import mitw.bungee.jedis.JedisSubscriptionHandler;
import mitw.bungee.util.FastUUID;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class MitwSubscriptionHandler implements JedisSubscriptionHandler {

    @Override
    public void handleMessage(JsonObject json) {
        String payload = json.get("payload").getAsString();

        JsonObject object = json.get("data").getAsJsonObject();

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

                String server = object.get("server").getAsString();
                long time = object.get("time").getAsLong();

                Mitw.INSTANCE.getKeepAliveHandler().handleKeepAlive(server, time);

                break;
        }
    }
}
