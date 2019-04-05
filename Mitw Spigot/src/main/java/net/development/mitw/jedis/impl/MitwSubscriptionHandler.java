package net.development.mitw.jedis.impl;

import com.google.gson.JsonObject;
import net.development.mitw.Mitw;
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

            case "LANGUAGE_CHANGED":

                UUID uuid = FastUUID.parseUUID(object.get("uuid").getAsString());
                String language = object.get("language").getAsString();

                Player player = Bukkit.getPlayer(uuid);

                Mitw.getInstance().getLanguageData().setLangWithoutSave(player, language, true);

                break;

        }
    }
}
