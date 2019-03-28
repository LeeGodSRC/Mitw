package net.development.mitw.jedis.impl;

import com.google.gson.JsonObject;
import net.development.mitw.Mitw;
import net.development.mitw.jedis.JedisSubscriptionHandler;
import org.bukkit.entity.Player;

public class MitwSubscriptionHandler implements JedisSubscriptionHandler {

    @Override
    public void handleMessage(JsonObject json) {
        switch (json.get("payload").getAsString()) {
        }
    }
}
