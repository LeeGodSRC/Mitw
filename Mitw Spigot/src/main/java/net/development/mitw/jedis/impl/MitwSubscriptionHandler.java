package net.development.mitw.jedis.impl;

import com.google.gson.JsonObject;
import net.development.mitw.Mitw;
import net.development.mitw.common.JedisPackets;
import net.development.mitw.jedis.JedisSubscriptionHandler;
import net.development.mitw.utils.FastUUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MitwSubscriptionHandler implements JedisSubscriptionHandler {

    @Override
    public void handleMessage(JsonObject json) {

        JsonObject object = json.get("data").getAsJsonObject();

        switch (json.get("payload").getAsString()) {

            case JedisPackets.LANGUAGE_CHANGED:

                UUID uuid = FastUUID.parseUUID(object.get("uuid").getAsString());
                String language = object.get("language").getAsString();

                Player player = Bukkit.getPlayer(uuid);

                if (player == null) {
                    return;
                }

                Mitw.getInstance().getLanguageData().setLangWithoutSave(player, language, true);

                break;

            case JedisPackets.KEEP_ALIVE:

                String server = object.get("server").getAsString();
                long time = object.get("time").getAsLong();

                Mitw.getInstance().getKeepAliveHandler().handleKeepAlive(server, time);

                break;

        }
    }
}
